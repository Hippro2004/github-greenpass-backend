package com.example.greenpass.v1.Stamp.repositories;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.greenpass.v1.Stamp.entities.Stamp;

public interface StampRepository extends JpaRepository<Stamp, Integer> {
        List<Stamp> findAllByUserUsername(String username);

        List<Stamp> findAllByUserUsernameAndParkParkId(String username, int parkId);

        boolean existsByUserUsernameAndParkParkIdAndStampDate(String username, Integer parkId, LocalDate now);

        Optional<Stamp> findTopByUserUsernameAndStampDateOrderByTimeDesc(String username, LocalDate now);

        Optional<Stamp> findTopByUserUsernameAndParkParkIdOrderByStampDateDescTimeDesc(String username, Integer parkId);

        long countByUserIsForeigner(boolean b);

        @Query("select function('YEAR', s.stampDate), function('MONTH', s.stampDate), s.user.isForeigner, count(s) "
                        + "from Stamp s group by function('YEAR', s.stampDate), function('MONTH', s.stampDate), s.user.isForeigner "
                        + "order by function('YEAR', s.stampDate), function('MONTH', s.stampDate)")
        List<Object[]> findVisitStatistics();

        @Query("select function('YEAR', s.stampDate), function('MONTH', s.stampDate), s.user.isForeigner, count(s) "
                        + "from Stamp s where s.park.parkId = :parkId group by function('YEAR', s.stampDate), function('MONTH', s.stampDate), s.user.isForeigner "
                        + "order by function('YEAR', s.stampDate), function('MONTH', s.stampDate)")
        List<Object[]> findVisitStatisticsByParkId(@Param("parkId") int parkId);

        @Query("SELECT s.park.parkId, s.user.isForeigner, COUNT(s) FROM Stamp s " +
               "WHERE (:year IS NULL OR function('YEAR', s.stampDate) = :year) " +
               "AND (:month IS NULL OR function('MONTH', s.stampDate) = :month) " +
               "GROUP BY s.park.parkId, s.user.isForeigner")
        List<Object[]> countVisitorsByParkAndFilters(@Param("year") Integer year, @Param("month") Integer month);
}

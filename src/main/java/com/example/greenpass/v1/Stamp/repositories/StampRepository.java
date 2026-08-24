package com.example.greenpass.v1.Stamp.repositories;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.greenpass.v1.Stamp.entities.Stamp;

public interface StampRepository extends JpaRepository<Stamp, Integer> {
    List<Stamp> findAllByUserUsername(String username);

    List<Stamp> findAllByUserUsernameAndParkParkId(String username, int parkId);

    boolean existsByUserUsernameAndParkParkIdAndStampDate(String username, Integer parkId, LocalDate now);

    long countByUserIsForeigner(boolean b);

    @Query("select function('YEAR', s.stampDate), function('MONTH', s.stampDate), s.user.isForeigner, count(s) "
            + "from Stamp s group by function('YEAR', s.stampDate), function('MONTH', s.stampDate), s.user.isForeigner "
            + "order by function('YEAR', s.stampDate), function('MONTH', s.stampDate)")
    List<Object[]> findVisitStatistics();
}

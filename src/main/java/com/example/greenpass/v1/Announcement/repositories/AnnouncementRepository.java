package com.example.greenpass.v1.Announcement.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.greenpass.v1.Announcement.entities.Announcement;

public interface AnnouncementRepository extends JpaRepository<Announcement, Integer> {

    @Query("SELECT a.park.parkId, COUNT(a) FROM Announcement a " +
           "WHERE (:year IS NULL OR function('YEAR', a.postDate) = :year) " +
           "AND (:month IS NULL OR function('MONTH', a.postDate) = :month) " +
           "GROUP BY a.park.parkId")
    List<Object[]> countAnnouncementsByParkAndFilters(@Param("year") Integer year, @Param("month") Integer month);
}

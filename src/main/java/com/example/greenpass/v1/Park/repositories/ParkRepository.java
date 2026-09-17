package com.example.greenpass.v1.Park.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.example.greenpass.v1.Park.entities.Park;

public interface ParkRepository extends JpaRepository<Park, Integer> {
    List<Park> findByNameContainingIgnoreCase(String keyword);
    boolean existsByName(String name);
    List<Park> findByName(String name);
    Optional<Park> findFirstByName(String name);

    @Modifying
    @Transactional
    @Query(value = "UPDATE park SET park_id = :newId WHERE park_id = :oldId", nativeQuery = true)
    int updateParkId(@Param("oldId") int oldId, @Param("newId") int newId);

    @Modifying
    @Transactional
    @Query(value = "UPDATE report SET park_id = :newId WHERE park_id = :oldId", nativeQuery = true)
    int updateReportParkId(@Param("oldId") int oldId, @Param("newId") int newId);

    @Modifying
    @Transactional
    @Query(value = "UPDATE park_ranger SET park_id = :newId WHERE park_id = :oldId", nativeQuery = true)
    int updateParkRangerParkId(@Param("oldId") int oldId, @Param("newId") int newId);

    @Modifying
    @Transactional
    @Query(value = "UPDATE stamp SET park_id = :newId WHERE park_id = :oldId", nativeQuery = true)
    int updateStampParkId(@Param("oldId") int oldId, @Param("newId") int newId);
}

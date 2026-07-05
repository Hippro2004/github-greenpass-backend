package com.example.greenpass.v1.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.greenpass.v1.entities.Announcement;

public interface AnnouncementRepository extends JpaRepository<Announcement, Long> {

}

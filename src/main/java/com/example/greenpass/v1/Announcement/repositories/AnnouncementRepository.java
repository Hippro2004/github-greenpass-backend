package com.example.greenpass.v1.Announcement.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.greenpass.v1.Announcement.entities.Announcement;

public interface AnnouncementRepository extends JpaRepository<Announcement, Long> {

}

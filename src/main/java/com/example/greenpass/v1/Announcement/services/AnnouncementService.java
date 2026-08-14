package com.example.greenpass.v1.Announcement.services;

import java.util.List;

import com.example.greenpass.v1.Announcement.entities.Announcement;

public interface AnnouncementService {
    List<Announcement> getAllAnnouncements();

    Announcement getAnnouncementById(Long id);

}

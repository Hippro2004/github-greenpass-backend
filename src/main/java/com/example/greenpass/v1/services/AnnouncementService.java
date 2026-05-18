package com.example.greenpass.v1.services;

import java.util.List;

import com.example.greenpass.v1.entities.Announcement;

public interface AnnouncementService {
    List<Announcement> getAllAnnouncements();

    Announcement getAnnouncementById(Long id);

}

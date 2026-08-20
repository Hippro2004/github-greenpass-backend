package com.example.greenpass.v1.Announcement.services;

import java.util.List;

import com.example.greenpass.v1.Announcement.dtos.AnnouncementResponse;
import com.example.greenpass.v1.Announcement.entities.Announcement;

public interface AnnouncementService {
    List<AnnouncementResponse> getAllAnnouncements();

    Announcement getAnnouncementById(int id);

}

package com.example.greenpass.v1.Announcement.services;

import java.util.List;

import com.example.greenpass.v1.Announcement.dtos.AnnouncementResponse;

public interface AnnouncementService {
    List<AnnouncementResponse> getAllAnnouncements();

    AnnouncementResponse getAnnouncementById(int id);

}

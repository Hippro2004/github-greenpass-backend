package com.example.greenpass.v1.Announcement.services;

import java.util.List;

import com.example.greenpass.v1.Announcement.dtos.AddAnnouncementDto;
import com.example.greenpass.v1.Announcement.dtos.AnnouncementResponse;
import com.example.greenpass.v1.Announcement.entities.Announcement;

public interface AnnouncementService {
    List<AnnouncementResponse> getAllAnnouncements();

    AnnouncementResponse getAnnouncementById(int id);

    Announcement addAnnouncement(AddAnnouncementDto dto);

    void deleteAnnouncement(int id);

    Announcement updateAnnouncement(int id, AddAnnouncementDto dto);

}

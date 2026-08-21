package com.example.greenpass.v1.Announcement.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.greenpass.v1.Announcement.dtos.AnnouncementResponse;
import com.example.greenpass.v1.Announcement.entities.Announcement;
import com.example.greenpass.v1.Announcement.repositories.AnnouncementRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AnnouncementServiceImpl implements AnnouncementService {
    private final AnnouncementRepository announcementRepository;

    @Override
    public List<AnnouncementResponse> getAllAnnouncements() {
        return announcementRepository.findAll().stream()
                .filter(announcement -> announcement != null && announcement.getPark() != null)
                .map(announcement -> new AnnouncementResponse(
                        announcement.getAnnouncementId(),
                        announcement.getAnnouncementTitle(),
                        announcement.getPostDate(),
                        announcement.getDescription(),
                        announcement.getPark().getName()))
                .toList();
    }

    @Override
    public AnnouncementResponse getAnnouncementById(int id) {
        Announcement announcement = announcementRepository.findById(id).orElse(null);
        return new AnnouncementResponse(
                announcement.getAnnouncementId(),
                announcement.getAnnouncementTitle(),
                announcement.getPostDate(),
                announcement.getDescription(),
                announcement.getPark().getName());
    }

}

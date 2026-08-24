package com.example.greenpass.v1.Announcement.services;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.greenpass.v1.Announcement.dtos.AddAnnouncementDto;
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

    @Override
    public Announcement addAnnouncement(AddAnnouncementDto dto) {
        Announcement announcement = Announcement.builder()
                .announcementTitle(dto.getTitle())
                .description(dto.getContent())
                .postDate(LocalDate.parse(dto.getPublishDate()))
                .image(dto.getImage())
                .build();
        return announcementRepository.save(announcement);
    }

    @Override
    public void deleteAnnouncement(int id) {
        announcementRepository.deleteById(id);
    }

    @Override
    public Announcement updateAnnouncement(int id, AddAnnouncementDto dto) {
        Announcement announcement = announcementRepository.findById(id).orElse(null);
        if (announcement == null) {
            return null;
        }

        Announcement upDateannouncement = Announcement.builder()
                .announcementTitle(dto.getTitle())
                .description(dto.getContent())
                .postDate(LocalDate.parse(dto.getPublishDate()))
                .image(dto.getImage())
                .build();
        return announcementRepository.save(upDateannouncement);
    }

}

package com.example.greenpass.v1.Announcement.services;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.greenpass.v1.Announcement.dtos.AddAnnouncementDto;
import com.example.greenpass.v1.Announcement.dtos.AnnouncementResponse;
import com.example.greenpass.v1.Announcement.entities.Announcement;
import com.example.greenpass.v1.Announcement.repositories.AnnouncementRepository;
import com.example.greenpass.v1.Park.entities.Park;
import com.example.greenpass.v1.Park.services.ParkService;
import com.example.greenpass.v1.ParkRanger.entities.ParkRanger;
import com.example.greenpass.v1.ParkRanger.repositories.ParkRangerRepository;
import com.example.greenpass.utils.FileUtils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AnnouncementServiceImpl implements AnnouncementService {
    private final AnnouncementRepository announcementRepository;
    private final ParkRangerRepository parkRangerRepository;
    private final ParkService parkService;

    private String formatImage(Announcement announcement) {
        if (announcement == null || announcement.getImage() == null)
            return null;
        String img = announcement.getImage().trim();

        if (img.startsWith("http://") || img.startsWith("https://") || img.startsWith("data:image")) {
            return img;
        }

        String cleanName = FileUtils.extractFileName(img, "announcements");
        if (cleanName == null || cleanName.equalsIgnoreCase("news1.jpg") || cleanName.equalsIgnoreCase("src/news1.jpg") || cleanName.equalsIgnoreCase("default.jpg")) {
            return null;
        }

        return "/uploads/announcements/" + cleanName;
    }

    @Override
    public List<AnnouncementResponse> getAllAnnouncements() {
        return announcementRepository.findAll().stream()
                .filter(announcement -> announcement != null && announcement.getPark() != null)
                .peek(announcement -> {
                    if (announcement.getImage() != null && announcement.getImage().startsWith("data:image")) {
                        try {
                            String cleanName = FileUtils.extractFileName(announcement.getImage(), "announcements");
                            if (cleanName != null) {
                                announcement.setImage(cleanName);
                                announcementRepository.save(announcement);
                            }
                        } catch (Exception e) {}
                    }
                })
                .sorted((a, b) -> Integer.compare(b.getAnnouncementId(), a.getAnnouncementId()))
                .map(announcement -> new AnnouncementResponse(
                        announcement.getAnnouncementId(),
                        announcement.getAnnouncementTitle(),
                        announcement.getPostDate(),
                        announcement.getDescription(),
                        announcement.getPark().getName(),
                        announcement.getPark().getParkId(),
                        formatImage(announcement)))
                .toList();
    }

    @Override
    public AnnouncementResponse getAnnouncementById(int id) {
        Announcement announcement = announcementRepository.findById(id).orElse(null);
        if (announcement == null)
            return null;

        if (announcement.getImage() != null && announcement.getImage().startsWith("data:image")) {
            try {
                String cleanName = FileUtils.extractFileName(announcement.getImage(), "announcements");
                if (cleanName != null) {
                    announcement.setImage(cleanName);
                    announcementRepository.save(announcement);
                }
            } catch (Exception e) {}
        }
        return new AnnouncementResponse(
                announcement.getAnnouncementId(),
                announcement.getAnnouncementTitle(),
                announcement.getPostDate(),
                announcement.getDescription(),
                announcement.getPark() != null ? announcement.getPark().getName() : "อุทยานแห่งชาติ",
                announcement.getPark() != null ? announcement.getPark().getParkId() : 1,
                formatImage(announcement));
    }

    @Override
    public Announcement addAnnouncement(AddAnnouncementDto dto) {
        Park park = null;
        if (dto.getUsername() != null && !dto.getUsername().trim().isEmpty()) {
            ParkRanger ranger = parkRangerRepository.findByUsername(dto.getUsername().trim());
            if (ranger != null && ranger.getPark() != null) {
                park = ranger.getPark();
            }
        }
        if (park == null) {
            try {
                park = parkService.getParkById(1);
            } catch (Exception e) {
            }
        }

        LocalDate postDate = LocalDate.now();
        if (dto.getPublishDate() != null && !dto.getPublishDate().trim().isEmpty()) {
            try {
                postDate = LocalDate.parse(dto.getPublishDate().trim());
            } catch (Exception e) {
            }
        }

        String cleanImage = FileUtils.extractFileName(dto.getImage(), "announcements");
        if (cleanImage == null || cleanImage.trim().isEmpty()) {
            cleanImage = "news1.jpg";
        }

        Announcement announcement = Announcement.builder()
                .announcementTitle(dto.getTitle() != null ? dto.getTitle() : "ประกาศข่าวสารอุทยาน")
                .description(dto.getContent() != null ? dto.getContent() : "")
                .postDate(postDate)
                .image(cleanImage)
                .park(park)
                .build();
        return announcementRepository.save(announcement);
    }

    @Override
    public void deleteAnnouncement(int id) {
        Announcement announcement = announcementRepository.findById(id).orElse(null);
        if (announcement != null) {
            if (announcement.getImage() != null) {
                FileUtils.deleteFile(announcement.getImage(), "announcements");
            }
            announcementRepository.delete(announcement);
        }
    }

    @Override
    public Announcement updateAnnouncement(int id, AddAnnouncementDto dto) {
        Announcement announcement = announcementRepository.findById(id).orElse(null);
        if (announcement == null) {
            return null;
        }

        LocalDate postDate = LocalDate.now();
        if (dto.getPublishDate() != null && !dto.getPublishDate().trim().isEmpty()) {
            try {
                postDate = LocalDate.parse(dto.getPublishDate().trim());
            } catch (Exception e) {
            }
        }

        announcement.setAnnouncementTitle(dto.getTitle());
        announcement.setDescription(dto.getContent());
        announcement.setPostDate(postDate);
        if (dto.getImage() != null && !dto.getImage().trim().isEmpty() && !dto.getImage().equalsIgnoreCase("null") && !dto.getImage().equals("-")) {
            String newImage = FileUtils.extractFileName(dto.getImage(), "announcements");
            if (newImage != null && !newImage.trim().isEmpty() && !newImage.equalsIgnoreCase("news1.jpg")) {
                if (announcement.getImage() != null && !announcement.getImage().equals(newImage)) {
                    FileUtils.deleteFile(announcement.getImage(), "announcements");
                }
                announcement.setImage(newImage);
            }
        }

        return announcementRepository.save(announcement);
    }

}

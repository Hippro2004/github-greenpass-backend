package com.example.greenpass.v1.Announcement.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.greenpass.dtos.ResponseObject;
import com.example.greenpass.v1.Announcement.entities.Announcement;
import com.example.greenpass.v1.Announcement.services.AnnouncementService;

import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/announcement")
@RequiredArgsConstructor
public class AnnouncementController {
    private final AnnouncementService announcementService;

    @GetMapping("/all-announcement")
    public ResponseEntity<ResponseObject> getAllAnnouncements() {
        try {
            List<Announcement> announcements = announcementService.getAllAnnouncements();
            if (announcements.isEmpty()) {
                return new ResponseEntity<>(
                        new ResponseObject(false, "Announcements research isn't success", null),
                        HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(new ResponseObject(true, "Announcements fetched successfully", announcements),
                    HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseObject(false, "Announcements not found", null),
                    HttpStatus.INTERNAL_SERVER_ERROR);

        }

    }

    @GetMapping("/announcement-details")
    public ResponseEntity<ResponseObject> getAnnouncementDetails(@RequestParam Long id) {
        try {
            Announcement announcement = announcementService.getAnnouncementById(id);

            if (announcement == null) {
                return new ResponseEntity<>(
                        new ResponseObject(false, "Announcement research isn't success", null),
                        HttpStatus.NOT_FOUND);

            }
            return new ResponseEntity<>(new ResponseObject(true, "Announcement fetched successfully", announcement),
                    HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseObject(false, "Failed to get announcements", null),
                    HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }

}

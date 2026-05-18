package com.example.greenpass.v1.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.greenpass.dtos.ResponseObject;
import com.example.greenpass.v1.entities.Announcement;
import com.example.greenpass.v1.services.AnnouncementService;

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
                        new ResponseObject(false, "Announcements research is success", null),
                        HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(new ResponseObject(true, "Announcements fetched successfully", announcements),
                    HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseObject(false, "Announcements not found", null),
                    HttpStatus.INTERNAL_SERVER_ERROR);

        }

    }

}

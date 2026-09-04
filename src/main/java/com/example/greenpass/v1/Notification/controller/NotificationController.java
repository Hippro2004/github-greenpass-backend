package com.example.greenpass.v1.Notification.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.greenpass.dtos.ResponseObject;
import com.example.greenpass.v1.Notification.entities.Notification;
import com.example.greenpass.v1.Notification.services.NotificationService;

import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/notification")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping("/park/{parkId}")
    public ResponseEntity<ResponseObject> getNotificationPark(@PathVariable Integer parkId) {
        try {
            List<Notification> notification = notificationService.getNotificationPark(parkId);
            return new ResponseEntity<>(new ResponseObject(true, "Notification found successfully", notification),
                    HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseObject(false, "Internal Server Error", null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

}

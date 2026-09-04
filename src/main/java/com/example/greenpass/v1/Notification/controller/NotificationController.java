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
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/notification")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
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

    @GetMapping("/my-notifications")
    public ResponseEntity<ResponseObject> getNotificationUser(@RequestHeader("username") String username) {
        try {
            List<Notification> notification = notificationService.getNotificationUser(username);
            return new ResponseEntity<>(new ResponseObject(true, "Notification found successfully", notification),
                    HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseObject(false, "Internal Server Error", null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{notificationId}/read")
    public ResponseEntity<ResponseObject> markAsRead(@PathVariable("notificationId") Long notificationId) {
        try {
            notificationService.markAsRead(notificationId);
            return new ResponseEntity<>(new ResponseObject(true, "Notification marked as read successfully", null),
                    HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseObject(false, "Internal Server Error", null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}

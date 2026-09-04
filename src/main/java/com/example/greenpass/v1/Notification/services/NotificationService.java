package com.example.greenpass.v1.Notification.services;

import java.time.LocalDateTime;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.example.greenpass.v1.Notification.entities.Notification;
import com.example.greenpass.v1.Notification.repositories.NotificationRepository;
import com.example.greenpass.v1.Park.entities.Park;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final SimpMessagingTemplate messagingTemplate;
    private final NotificationRepository notificationRepository;

    public void sendParkNotification(Park park, String title, String message, int reportId) {
        Notification notification = Notification.builder()
                .title(title)
                .message(message)
                .reportId(reportId)
                .isRead(false)
                .createdAt(LocalDateTime.now())
                .park(park)
                .build();

        Notification saved = notificationRepository.save(notification);

        String destination = "/topic/park" + park.getParkId() + "notifications";
        messagingTemplate.convertAndSend(destination, saved);

    }

}

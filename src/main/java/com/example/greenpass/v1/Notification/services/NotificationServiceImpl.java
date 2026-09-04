package com.example.greenpass.v1.Notification.services;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.example.greenpass.v1.Notification.entities.Notification;
import com.example.greenpass.v1.Notification.repositories.NotificationRepository;
import com.example.greenpass.v1.Park.entities.Park;
import com.example.greenpass.v1.Report.entities.Report;
import com.example.greenpass.v1.User.entities.User;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor

public class NotificationServiceImpl implements NotificationService {
    private final SimpMessagingTemplate messagingTemplate;
    private final NotificationRepository notificationRepository;

    @Override
    public void sendParkNotification(Park park, String title, String message, Report report) {
        Notification notification = Notification.builder()
                .title(title)
                .message(message)
                .isRead(false)
                .createdAt(LocalDateTime.now())
                .report(report)
                .park(park)
                .build();

        Notification saved = notificationRepository.save(notification);

        String destination = "/topic/park" + park.getParkId() + "/notifications";
        messagingTemplate.convertAndSend(destination, saved);

    }

    @Override
    public void sendUserNotification(User user, String title, String message, Report report) {
        Notification notification = Notification.builder()
                .title(title)
                .message(message)
                .isRead(false)
                .createdAt(LocalDateTime.now())
                .report(report)
                .user(user)
                .build();

        Notification saved = notificationRepository.save(notification);

        String destination = "/topic/user" + user.getUsername() + "/notifications";
        messagingTemplate.convertAndSend(destination, saved);

        if (user != null && user.getFcmToken() != null && !user.getFcmToken().isBlank()) {
            try {
                Message fcmMessage = Message.builder()
                        .setToken(user.getFcmToken())
                        .setNotification(com.google.firebase.messaging.Notification.builder()
                                .setTitle(title)
                                .setBody(message)
                                .build())
                        .putData("reportId", String.valueOf(report.getReportId()))
                        .build();

                FirebaseMessaging.getInstance().send(fcmMessage);

            } catch (Exception e) {
                System.err.println("Failed to send Firebase notification: " + e.getMessage());
            }
        }

    }

    @Override
    public List<Notification> getNotificationPark(int parkId) {
        return notificationRepository.findAllByParkParkIdOrderByCreatedAtDesc(parkId);
    }
}

package com.example.greenpass.v1.Notification.services;

import java.util.List;

import com.example.greenpass.v1.Notification.entities.Notification;
import com.example.greenpass.v1.Park.entities.Park;
import com.example.greenpass.v1.Report.entities.Report;
import com.example.greenpass.v1.User.entities.User;

public interface NotificationService {

    void sendParkNotification(Park park, String title, String message, Report report);

    void sendUserNotification(User user, String title, String message, Report report);

    List<Notification> getNotificationPark(int parkId);

    List<Notification> getNotificationUser(String username);

    void markAsRead(Long notificationId);
}

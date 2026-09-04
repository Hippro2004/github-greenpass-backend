package com.example.greenpass.v1.Notification.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.greenpass.v1.Notification.entities.Notification;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

}

package com.example.greenpass.v1.Notification.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.greenpass.v1.Notification.entities.Notification;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findAllByParkParkIdOrderByCreatedAtDesc(int parkId);

    List<Notification> findAllByUserUsernameOrderByCreatedAtDesc(String username);

    long countByParkParkIdAndIsReadFalse(Integer parkId);

    long countByUserUsernameAndIsReadFalse(String username);
}

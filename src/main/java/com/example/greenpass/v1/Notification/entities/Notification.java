package com.example.greenpass.v1.Notification.entities;

import java.time.LocalDateTime;

import com.example.greenpass.v1.Park.entities.Park;
import com.example.greenpass.v1.Report.entities.Report;
import com.example.greenpass.v1.User.entities.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long notificationId;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String message;

    private boolean isRead;

    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "reportId", nullable = false)
    private Report report;

    @ManyToOne
    @JoinColumn(name = "parkId", nullable = true)
    private Park park;

    @ManyToOne
    @JoinColumn(name = "username", nullable = true)
    private User user;

}

package com.example.greenpass.v1.Reward.entities;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Reward {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer rewardId;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String rewardTitle;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String rewardDetails;

    @Column(nullable = false)
    private LocalDate rewardAnnouncementDate;

    @Column(nullable = false, columnDefinition = "LONGTEXT")
    private String image;
}

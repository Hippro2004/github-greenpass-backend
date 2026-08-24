package com.example.greenpass.v1.Announcement.entities;

import java.time.LocalDate;

import com.example.greenpass.v1.Park.entities.Park;

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

@Entity
@Table
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Announcement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer announcementId;

    @Column(nullable = false)
    private String announcementTitle;

    @Column(nullable = false)
    private LocalDate postDate;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private String image;

    @ManyToOne
    @JoinColumn(name = "parkId", nullable = false)
    private Park park;

}

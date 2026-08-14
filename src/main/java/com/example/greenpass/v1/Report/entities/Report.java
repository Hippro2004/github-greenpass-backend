package com.example.greenpass.v1.Report.entities;

import java.time.LocalDate;

import com.example.greenpass.v1.Park.entities.Park;
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

@Entity
@Table
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer reportId;

    @Column(nullable = false, length = 25)
    private String name;

    @Column(nullable = false)
    private LocalDate reportDate;

    @Column(nullable = false)
    private String description;

    private String image;

    @Column(nullable = false)
    private String status;

    @ManyToOne
    @JoinColumn(name = "parkId", nullable = false)
    private Park park;

    @ManyToOne
    @JoinColumn(name = "username", nullable = false)
    private User user;
}

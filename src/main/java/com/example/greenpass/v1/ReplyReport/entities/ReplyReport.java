package com.example.greenpass.v1.ReplyReport.entities;

import java.time.LocalDate;
import java.time.LocalTime;

import com.example.greenpass.v1.ParkRanger.entities.ParkRanger;
import com.example.greenpass.v1.Report.entities.Report;

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
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReplyReport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer replyReportId;

    @Column(nullable = false)
    private LocalDate updateDate;

    @Column(nullable = false)
    private LocalTime updateTime;

    private String progress;

    @Column(nullable = false)
    private String currentStatus;

    private String image;

    @ManyToOne
    @JoinColumn(name = "reportId", nullable = false)
    private Report report;

    @ManyToOne
    @JoinColumn(name = "parkRangerId")
    private ParkRanger parkRanger;

}

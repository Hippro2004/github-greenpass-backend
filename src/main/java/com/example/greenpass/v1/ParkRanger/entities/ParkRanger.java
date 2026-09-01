package com.example.greenpass.v1.ParkRanger.entities;

import java.time.LocalDate;

import com.example.greenpass.v1.Park.entities.Park;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
public class ParkRanger {
    @Id
    @Column(nullable = false, length = 50)
    private String username;

    @Column(nullable = false, length = 16)
    private String password;

    @Column(nullable = false, length = 50)
    private String firstname;

    @Column(nullable = false, length = 50)
    private String surname;

    @Column(nullable = false, length = 10)
    private String mobilephone;

    @Column(nullable = false, length = 50, unique = true)
    private String email;

    @Column(nullable = false)
    private LocalDate birthDate;

    @Column(nullable = false, length = 1)
    private Integer gender;

    @Column(nullable = false)
    private String signature;

    @Column(nullable = false, length = 20)
    private String district;

    @Column(nullable = false, length = 20)
    private String subDistrict;

    @Column(nullable = false, length = 20)
    private String province;

    @Column(length = 5, nullable = false)
    private String zipcode;

    @Column(nullable = false, length = 25)
    private String position;

    @Column(nullable = false)
    private LocalDate startDate;

    private boolean canAnnouncement;
    private boolean canIssueStamp;
    private boolean canProgressReport;
    private boolean canEditParkDetails;

    @ManyToOne
    @JoinColumn(name = "parkId", nullable = false)
    @JsonIgnoreProperties({"parkRangers", "stamps"})
    private Park park;
}

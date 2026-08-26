package com.example.greenpass.v1.Park.entities;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import com.example.greenpass.v1.ParkRanger.entities.ParkRanger;
import com.example.greenpass.v1.Stamp.entities.Stamp;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Park {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer parkId;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String image;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String location;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private LocalTime openTime;

    @Column(nullable = false)
    private LocalTime closeTime;

    private boolean isSeasonalPark;

    private LocalDate seasonOpenDate;
    private LocalDate seasonCloseDate;

    private boolean isTemporaryClosed;

    @Column(nullable = false)
    private String eventNote;

    @Column(nullable = false)
    private String status;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "parkId")
    @JsonManagedReference(value = "park-stamps")
    private List<Stamp> stamps = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "park")
    @JsonManagedReference(value = "park-rangers")
    private List<ParkRanger> parkRangers = new ArrayList<>();

}

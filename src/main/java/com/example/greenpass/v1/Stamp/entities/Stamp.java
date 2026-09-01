package com.example.greenpass.v1.Stamp.entities;

import java.time.LocalDate;

import com.example.greenpass.v1.Park.entities.Park;
import com.example.greenpass.v1.ParkRanger.entities.ParkRanger;
import com.example.greenpass.v1.User.entities.User;
import com.fasterxml.jackson.annotation.JsonBackReference;

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
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Stamp {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer stampId;

    @Column(nullable = false)
    private LocalDate stampDate;

    @ManyToOne
    @JoinColumn(name = "username", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "parkId", nullable = false)
    @JsonBackReference(value = "park-stamps")
    private Park park;

    @ManyToOne
    @JoinColumn(name = "usernameParkRanger", nullable = false)
    private ParkRanger parkRanger;

}

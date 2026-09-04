package com.example.greenpass.v1.User.entities;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
public class User {

    @Id
    @Column(nullable = false, length = 50)
    private String username;

    @Column(nullable = false, length = 16)
    private String password;

    @Column(nullable = false, length = 50)
    private String firstname;

    @Column(nullable = false, length = 50)
    private String lastname;

    @Column(nullable = false, length = 10, unique = true)
    private String phone;

    @Column(nullable = false, length = 50, unique = true)
    private String email;

    @Column(nullable = false)
    private LocalDate birthDate;

    @Column(nullable = false, length = 1)
    private Integer gender;

    private boolean isForeigner;

    @Column(length = 20)
    private String district;

    @Column(length = 20)
    private String subDistrict;

    @Column(length = 20)
    private String province;

    @Column(length = 5)
    private String zipcode;

    @Column(length = 255)
    private String fcmToken;

}

package com.example.greenpass.v1.User.dtos;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateUserDto {
    private String firstname;
    private String lastname;
    private String profileImage;
    private String email;
    private String phone;
    private LocalDate birthDate;
    private Integer gender;
    private boolean isForeigner;
    private String district;
    private String subDistrict;
    private String province;
    private String zipcode;
    private String password;
}
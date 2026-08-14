package com.example.greenpass.v1.User.dtos;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

import lombok.AllArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserDto {
    private String firstname;
    private String lastname;
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
package com.example.greenpass.v1.ParkRanger.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddParkRangerDto {

    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String birthDate;
    private String position;
    private String startDate;
    private String district;
    private String subDistrict;
    private String province;
    private Integer gender;
    private String phone;
    private String email;
    private String zipcode;

    private Integer parkId;

    private Boolean canAnnouncement;
    private Boolean canIssueStamp;
    private Boolean canProgressReport;
    private Boolean canEditParkDetails;

}

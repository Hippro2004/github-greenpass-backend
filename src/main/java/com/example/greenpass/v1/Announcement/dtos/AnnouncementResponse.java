package com.example.greenpass.v1.Announcement.dtos;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AnnouncementResponse {

    private Integer announcementId;
    private String announcementTitle;
    private LocalDate postDate;
    private String description;
    private String parkName;
    private Integer parkId;

}

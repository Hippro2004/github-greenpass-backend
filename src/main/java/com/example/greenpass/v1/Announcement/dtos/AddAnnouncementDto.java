package com.example.greenpass.v1.Announcement.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddAnnouncementDto {
    private String title;
    private String content;
    private String publishDate;
    private String username;
    private String image;
}

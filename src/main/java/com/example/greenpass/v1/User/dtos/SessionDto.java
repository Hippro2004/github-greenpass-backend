package com.example.greenpass.v1.User.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SessionDto {
    private String username;
    private String firstname;
    private String lastname;

}

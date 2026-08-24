package com.example.greenpass.v1.Admin.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginAdminDto {
    private String username;
    private String password;
}

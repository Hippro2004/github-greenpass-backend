package com.example.greenpass.v1.Stamp.dtos;

import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class QrResponse {
    private String qrBase64;
    private long expireAt;
}
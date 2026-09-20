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
    private long expiresInSeconds;

    public QrResponse(String qrBase64, long expireAt) {
        this.qrBase64 = qrBase64;
        this.expireAt = expireAt;
        this.expiresInSeconds = 300;
    }
}
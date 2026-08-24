package com.example.greenpass.v1.Stamp.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.greenpass.dtos.ResponseObject;
import com.example.greenpass.v1.Stamp.dtos.QrResponse;
import com.example.greenpass.v1.Stamp.dtos.StampResponse;
import com.example.greenpass.v1.Stamp.dtos.VisitStatisticsResponse;
import com.example.greenpass.v1.Stamp.entities.Stamp;
import com.example.greenpass.v1.Stamp.services.JwtService;
import com.example.greenpass.v1.Stamp.services.QRService;
import com.example.greenpass.v1.Stamp.services.StampService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/stamp")
@RequiredArgsConstructor
public class StampController {

    private final QRService qrService;
    private final JwtService jwtService;
    private final StampService stampService;

    @GetMapping("/qr")
    public ResponseEntity<ResponseObject> getQr(@RequestHeader("username") String username) {
        try {
            int expireMinutes = 5;
            String token = jwtService.generateToken(username, expireMinutes);
            String qrBase64 = qrService.generateQr(token);
            long expireAt = System.currentTimeMillis() + (expireMinutes * 60 * 1000L);

            return new ResponseEntity<>(
                    new ResponseObject(true, "QR generated", new QrResponse(qrBase64, expireAt)),
                    HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(
                    new ResponseObject(false, "Failed to generate QR", null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // @PostMapping("/scan")
    // public ResponseEntity<ResponseObject> scanQr(@RequestBody ScanQrDto
    // scanQrDto) {
    // try {
    // if (jwtService.isTokenExpired(scanQrDto.getToken())) {
    // return new ResponseEntity<>(
    // new ResponseObject(false, "QR หมดอายุแล้ว", null),
    // HttpStatus.UNAUTHORIZED);
    // }

    // // String userId = jwtService.extractUserId(scanQrDto.getToken());

    // return new ResponseEntity<>(
    // new ResponseObject(true, "ประทับแสตมป์สำเร็จ", null),
    // HttpStatus.OK);
    // } catch (Exception e) {
    // return new ResponseEntity<>(
    // new ResponseObject(false, "เกิดข้อผิดพลาด", null),
    // HttpStatus.INTERNAL_SERVER_ERROR);
    // }
    // }

    @GetMapping("/my-stamps")
    public ResponseEntity<ResponseObject> getMyStamps(@RequestHeader("username") String username) {
        try {
            List<Stamp> rawStamps = stampService.getAllStampsByUsername(username);
            List<StampResponse> stamps = rawStamps.stream()
                    .filter(stamp -> stamp != null && stamp.getPark() != null)
                    .map(stamp -> new StampResponse(
                            stamp.getStampId(),
                            stamp.getStampDate(),
                            stamp.getPark().getParkId(),
                            stamp.getPark().getName()))
                    .toList();
            return new ResponseEntity<>(
                    new ResponseObject(true, "Success", stamps),
                    HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(
                    new ResponseObject(false, "Failed to load stamps", null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/stamp-details")
    public ResponseEntity<ResponseObject> getStampDetails(
            @RequestHeader("username") String username,
            @RequestParam("parkId") int parkId) {
        try {
            List<Stamp> histories = stampService.getAllStampsByUsernameAndParkId(username, parkId);
            return new ResponseEntity<>(new ResponseObject(true, "Success", histories), HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(
                    new ResponseObject(false, "Failed to ", null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/statistics")
    public ResponseEntity<ResponseObject> getStatistics() {
        try {
            VisitStatisticsResponse stats = stampService.getVisitStatistics();
            return new ResponseEntity<>(
                    new ResponseObject(true, "Fetch visit statistics successfully", stats),
                    HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(
                    new ResponseObject(false, "Failed to fetch visit statistics", null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}

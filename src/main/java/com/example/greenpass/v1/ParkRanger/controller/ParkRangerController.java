package com.example.greenpass.v1.ParkRanger.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.greenpass.dtos.ResponseObject;
import com.example.greenpass.v1.ParkRanger.dtos.AddParkRangerDto;
import com.example.greenpass.v1.ParkRanger.dtos.LoginParkRangerDto;
import com.example.greenpass.v1.ParkRanger.dtos.ParkRangerResponseDto;
import com.example.greenpass.v1.ParkRanger.entities.ParkRanger;
import com.example.greenpass.v1.ParkRanger.services.ParkRangerService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/ranger")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class ParkRangerController {

    private final ParkRangerService parkRangerService;

    @PostMapping("/login")
    public ResponseEntity<ResponseObject> login(@RequestBody @Valid LoginParkRangerDto loginParkRangerDto) {
        try {
            String reqUsername = loginParkRangerDto.getUsername() != null ? loginParkRangerDto.getUsername().trim()
                    : "";
            String reqPassword = loginParkRangerDto.getPassword() != null ? loginParkRangerDto.getPassword().trim()
                    : "";

            ParkRanger ranger = parkRangerService.getParkRangerByUsername(reqUsername);
            if (ranger != null) {
                if (ranger.getPassword() != null && ranger.getPassword().trim().equals(reqPassword)) {
                    ParkRangerResponseDto parkRangerdto = ParkRangerResponseDto.builder()
                            .username(ranger.getUsername())
                            .firstname(ranger.getFirstname())
                            .surname(ranger.getSurname())
                            .email(ranger.getEmail())
                            .mobilephone(ranger.getMobilephone())
                            .position(ranger.getPosition())
                            .parkId(ranger.getPark().getParkId())
                            .signature(ranger.getSignature())
                            .build();

                    return new ResponseEntity<>(
                            new ResponseObject(true, "Park Ranger Login Successfully", parkRangerdto),
                            HttpStatus.OK);
                }

                return new ResponseEntity<>(new ResponseObject(false, "Password incorrect", null),
                        HttpStatus.UNAUTHORIZED);

            }

            return new ResponseEntity<>(new ResponseObject(false, "Park Ranger not found", null),
                    HttpStatus.NOT_FOUND);

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new ResponseObject(false, "Failed to Login", null),
                    HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }

    @PostMapping("/add")
    public ResponseEntity<ResponseObject> addRanger(@RequestBody @Valid AddParkRangerDto addParkRangerDto) {
        try {
            ParkRanger newRanger = parkRangerService.addParkRanger(addParkRangerDto);

            return new ResponseEntity<>(new ResponseObject(true, "Add Park Ranger Successfully", newRanger),
                    HttpStatus.CREATED);

        } catch (IllegalArgumentException e) {

            return new ResponseEntity<>(new ResponseObject(false, e.getMessage(), null),
                    HttpStatus.BAD_REQUEST);

        } catch (Exception e) {

            e.printStackTrace();
            return new ResponseEntity<>(new ResponseObject(false, "Failed to Add Park Ranger", null),
                    HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }

    @GetMapping("/all")
    public ResponseEntity<ResponseObject> getAllRangers() {
        try {
            List<ParkRanger> rangers = parkRangerService.getAllParkRangers();
            return new ResponseEntity<>(new ResponseObject(true, "Fetch All Park Rangers Successfully", rangers),
                    HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new ResponseObject(false, "Failed to Fetch Park Rangers", null),
                    HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }

    @PostMapping("/set-role")
    public ResponseEntity<ResponseObject> setRole(
            @RequestParam String username,
            @RequestBody AddParkRangerDto dto) {
        try {
            // Alternate Flow 3.1: กรณีที่ผู้ดูแลระบบไม่ได้เลือก Role ระบบจะแสดงข้อความ “กรุณาเลือก Role 1 รายการ”
            boolean hasAtLeastOneRole = Boolean.TRUE.equals(dto.getCanAnnouncement())
                    || Boolean.TRUE.equals(dto.getCanIssueStamp())
                    || Boolean.TRUE.equals(dto.getCanProgressReport())
                    || Boolean.TRUE.equals(dto.getCanEditParkDetails());

            if (!hasAtLeastOneRole) {
                return new ResponseEntity<>(
                        new ResponseObject(false, "กรุณาเลือก Role 1 รายการ", null),
                        HttpStatus.BAD_REQUEST);
            }

            ParkRanger ranger = parkRangerService.updateParkRanger(username, dto);
            if (ranger == null) {
                // Alternate Flow 5.1.1: กรณีที่ระบบไม่สามารถบันทึกข้อมูลได้
                return new ResponseEntity<>(
                        new ResponseObject(false, "ไม่สามารถบันทึกข้อมูลได้กรุณาลองใหม่อีกครั้ง", null),
                        HttpStatus.INTERNAL_SERVER_ERROR);
            }

            return new ResponseEntity<>(
                    new ResponseObject(true, "กำหนดสิทธิ์ให้ Park Ranger สำเร็จ", ranger),
                    HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            // Alternate Flow 5.1.1: กรณีที่ระบบไม่สามารถบันทึกข้อมูลได้
            return new ResponseEntity<>(
                    new ResponseObject(false, "ไม่สามารถบันทึกข้อมูลได้กรุณาลองใหม่อีกครั้ง", null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/update")
    public ResponseEntity<ResponseObject> updateRanger(
            @RequestParam String username,
            @RequestBody @Valid AddParkRangerDto dto) {
        try {
            // Alternate Flow 3.1: หากมีการส่งข้อมูลสิทธิ์และไม่ได้เลือกสักรายการ
            if (dto.getCanAnnouncement() != null && dto.getCanIssueStamp() != null
                    && dto.getCanEditParkDetails() != null && dto.getCanProgressReport() != null) {
                if (!dto.getCanAnnouncement() && !dto.getCanIssueStamp()
                        && !dto.getCanEditParkDetails() && !dto.getCanProgressReport()) {
                    return new ResponseEntity<>(
                            new ResponseObject(false, "กรุณาเลือก Role 1 รายการ", null),
                            HttpStatus.BAD_REQUEST);
                }
            }

            ParkRanger ranger = parkRangerService.updateParkRanger(username, dto);
            if (ranger == null) {
                return new ResponseEntity<>(new ResponseObject(false, "ไม่สามารถบันทึกข้อมูลได้กรุณาลองใหม่อีกครั้ง", null),
                        HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(
                    new ResponseObject(true, "Park Ranger updated successfully", ranger),
                    HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new ResponseObject(false, "ไม่สามารถบันทึกข้อมูลได้กรุณาลองใหม่อีกครั้ง", null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/get")
    public ResponseEntity<ResponseObject> getRangerByUsername(@RequestParam String username) {
        try {
            ParkRanger ranger = parkRangerService.getParkRangerByUsername(username);
            if (ranger == null) {
                return new ResponseEntity<>(new ResponseObject(false, "Park Ranger not found", null),
                        HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(
                    new ResponseObject(true, "Fetch Park Ranger Successfully", ranger),
                    HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new ResponseObject(false, "Failed to Fetch Park Ranger", null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}

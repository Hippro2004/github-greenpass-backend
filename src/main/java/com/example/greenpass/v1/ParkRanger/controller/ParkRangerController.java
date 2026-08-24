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

    @PostMapping("/update")
    public ResponseEntity<ResponseObject> updateRanger(
            @RequestParam String username,
            @RequestBody @Valid AddParkRangerDto dto) {
        try {
            ParkRanger ranger = parkRangerService.updateParkRanger(username, dto);
            if (ranger == null) {
                return new ResponseEntity<>(new ResponseObject(false, "Park Ranger not found", null),
                        HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(
                    new ResponseObject(true, "Park Ranger updated successfully", ranger),
                    HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new ResponseObject(false, "Failed to update Park Ranger", null),
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

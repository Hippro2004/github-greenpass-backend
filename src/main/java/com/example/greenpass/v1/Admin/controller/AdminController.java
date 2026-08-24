package com.example.greenpass.v1.Admin.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.greenpass.dtos.ResponseObject;
import com.example.greenpass.v1.Admin.dtos.LoginAdminDto;
import com.example.greenpass.v1.Admin.entities.Admin;
import com.example.greenpass.v1.Admin.services.AdminService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/admin")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @PostMapping("/login")
    public ResponseEntity<ResponseObject> login(@RequestBody @Valid LoginAdminDto loginAdminDto) {
        try {
            Admin admin = adminService.getAdmin(loginAdminDto.getUsername());

            if (admin != null) {
                if (admin.getPassword().equalsIgnoreCase(loginAdminDto.getPassword())) {
                    return new ResponseEntity<>(new ResponseObject(true, "Admin Login Successfully", admin),
                            HttpStatus.OK);
                }

                return new ResponseEntity<>(new ResponseObject(false, "Password incorrect", null),
                        HttpStatus.UNAUTHORIZED);
            }
            return new ResponseEntity<>(new ResponseObject(false, "Admin not found", null),
                    HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new ResponseObject(false, "Failed to Login", null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
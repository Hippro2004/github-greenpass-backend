package com.example.greenpass.v1.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.greenpass.dtos.ResponseObject;
import com.example.greenpass.v1.entities.Park;
import com.example.greenpass.v1.services.ParkService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/park")
@RequiredArgsConstructor
public class ParkController {
    private final ParkService parkService;

    @GetMapping("/search")
    public ResponseEntity<ResponseObject> search(
            @RequestParam(defaultValue = "") String keyword) {
        try {
            List<Park> parks = parkService.searchByName(keyword);
            // ResponseObject responseObject = new ResponseObject(true, "Success", parks);
            // ResponseEntity<ResponseObject> responseEntity = new
            // ResponseEntity<>(responseObject, HttpStatus.OK);
            // return responseEntity;
            return new ResponseEntity<>(
                    new ResponseObject(true, "Success", parks),
                    HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(
                    new ResponseObject(false, "An error occurred", null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

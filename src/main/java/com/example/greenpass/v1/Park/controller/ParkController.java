package com.example.greenpass.v1.Park.controller;

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
import com.example.greenpass.v1.Park.dtos.SearchParkDto;
import com.example.greenpass.v1.Park.entities.Park;
import com.example.greenpass.v1.Park.services.ParkService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/park")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class ParkController {
        private final ParkService parkService;

        @GetMapping("/search")
        public ResponseEntity<ResponseObject> search(
                        @RequestParam(defaultValue = "") String keyword) {
                try {
                        List<Park> parks = parkService.searchByName(keyword);
                        List<SearchParkDto> parkDtos = parks.stream()
                                        .map(p -> SearchParkDto.builder()
                                                        .id(p.getParkId())
                                                        .parkId(p.getParkId())
                                                        .name(p.getName())
                                                        .image(p.getImage())
                                                        .address(p.getAddress())
                                                        .location(p.getLocation())
                                                        .description(p.getDescription())
                                                        .openTime(p.getOpenTime())
                                                        .closeTime(p.getCloseTime())
                                                        .isSeasonalPark(p.isSeasonalPark())
                                                        .seasonOpenDate(p.getSeasonOpenDate())
                                                        .seasonCloseDate(p.getSeasonCloseDate())
                                                        .isTemporaryClosed(p.isTemporaryClosed())
                                                        .eventNote(p.getEventNote())
                                                        .status(p.getStatus())
                                                        .build())
                                        .toList();
                        // ResponseObject responseObject = new ResponseObject(true, "Success", parks);
                        // ResponseEntity<ResponseObject> responseEntity = new
                        // ResponseEntity<>(responseObject, HttpStatus.OK);
                        // return responseEntity;
                        return new ResponseEntity<>(
                                        new ResponseObject(true, "Success", parkDtos),
                                        HttpStatus.OK);
                } catch (Exception e) {
                        e.printStackTrace();
                        return new ResponseEntity<>(
                                        new ResponseObject(false, "An error occurred", null),
                                        HttpStatus.INTERNAL_SERVER_ERROR);

                }
        }

        @GetMapping("/get")
        public ResponseEntity<ResponseObject> getParkById(@RequestParam("parkId") int id) {
                try {
                        Park park = parkService.getParkById(id);

                        if (park == null) {
                                return new ResponseEntity<>(
                                                new ResponseObject(false, "Park not found", null),
                                                HttpStatus.NOT_FOUND);
                        }

                        SearchParkDto parkDto = SearchParkDto.builder()
                                        .id(park.getParkId())
                                        .parkId(park.getParkId())
                                        .name(park.getName())
                                        .image(park.getImage())
                                        .address(park.getAddress())
                                        .location(park.getLocation())
                                        .description(park.getDescription())
                                        .openTime(park.getOpenTime())
                                        .closeTime(park.getCloseTime())
                                        .isSeasonalPark(park.isSeasonalPark())
                                        .seasonOpenDate(park.getSeasonOpenDate())
                                        .seasonCloseDate(park.getSeasonCloseDate())
                                        .isTemporaryClosed(park.isTemporaryClosed())
                                        .status(park.getStatus())
                                        .eventNote(park.getEventNote())
                                        .build();

                        return new ResponseEntity<>(
                                        new ResponseObject(true, "Success", parkDto),
                                        HttpStatus.OK);
                } catch (Exception e) {
                        e.printStackTrace();
                        return new ResponseEntity<>(
                                        new ResponseObject(false, "An error occurred", null),
                                        HttpStatus.INTERNAL_SERVER_ERROR);

                }
        }

        @PostMapping("/update")
        public ResponseEntity<ResponseObject> updatePark(@RequestBody Park park) {
                try {
                        Park updated = parkService.saveOrUpdatePark(park);
                        return new ResponseEntity<>(
                                        new ResponseObject(true, "Park updated successfully", updated),
                                        HttpStatus.OK);
                } catch (Exception e) {
                        return new ResponseEntity<>(
                                        new ResponseObject(false, "Error updating park: " + e.getMessage(), null),
                                        HttpStatus.INTERNAL_SERVER_ERROR);
                }
        }

}

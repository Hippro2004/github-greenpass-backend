package com.example.greenpass.v1.Park.services;

import java.util.List;

import com.example.greenpass.v1.Park.entities.Park;

public interface ParkService {
    Park getParkById(int id);

    List<Park> getAllParkByKeyword(String keyword);

    List<Park> searchByName(String keyword);

    Park saveOrUpdatePark(Park park);
}
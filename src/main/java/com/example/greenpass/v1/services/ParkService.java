package com.example.greenpass.v1.services;

import java.util.List;

import com.example.greenpass.v1.entities.Park;

public interface ParkService {
    Park getParkById(int id);

    List<Park> getAllParkByKeyword(String keyword);

    List<Park> searchByName(String keyword);
}
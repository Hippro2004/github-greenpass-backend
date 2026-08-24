package com.example.greenpass.v1.ParkRanger.services;

import java.util.List;

import com.example.greenpass.v1.ParkRanger.dtos.AddParkRangerDto;
import com.example.greenpass.v1.ParkRanger.entities.ParkRanger;

public interface ParkRangerService {
    ParkRanger getParkRangerByUsername(String username);

    ParkRanger addParkRanger(AddParkRangerDto dto);

    List<ParkRanger> getAllParkRangers();

    ParkRanger updateParkRanger(String username, AddParkRangerDto dto);
}

package com.example.greenpass.v1.services;

import com.example.greenpass.v1.entities.ParkRanger;

public interface ParkRangerService {
    ParkRanger getParkRangerByUsername(String username);
}

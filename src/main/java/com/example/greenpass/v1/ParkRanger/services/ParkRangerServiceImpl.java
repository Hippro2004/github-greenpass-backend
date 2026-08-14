package com.example.greenpass.v1.ParkRanger.services;

import org.springframework.stereotype.Service;

import com.example.greenpass.v1.ParkRanger.entities.ParkRanger;
import com.example.greenpass.v1.ParkRanger.repositories.ParkRangerRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ParkRangerServiceImpl implements ParkRangerService {
    private ParkRangerRepository parkRangerRepository;

    @Override
    public ParkRanger getParkRangerByUsername(String username) {
        return parkRangerRepository.findByUsername(username);
    }

}

package com.example.greenpass.v1.Park.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.greenpass.v1.Park.entities.Park;
import com.example.greenpass.v1.Park.repositories.ParkRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ParkServiceImpl implements ParkService {
    private final ParkRepository parkRepository;

    @Override
    public Park getParkById(int id) {
        return parkRepository.findById(id).orElseThrow();

    }

    @Override
    public List<Park> getAllParkByKeyword(String keyword) {
        return parkRepository.findAll();
    }

    @Override
    public List<Park> searchByName(String keyword) {
        return parkRepository.findByNameContainingIgnoreCase(keyword);
    }

}

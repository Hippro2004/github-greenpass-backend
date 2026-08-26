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

    @Override
    public Park saveOrUpdatePark(Park park) {
        if (park.getParkId() != null) {
            Park existing = parkRepository.findById(park.getParkId()).orElse(null);
            if (existing != null) {
                if (park.getName() != null) existing.setName(park.getName());
                if (park.getImage() != null) existing.setImage(park.getImage());
                if (park.getAddress() != null) existing.setAddress(park.getAddress());
                if (park.getLocation() != null) existing.setLocation(park.getLocation());
                if (park.getDescription() != null) existing.setDescription(park.getDescription());
                if (park.getOpenTime() != null) existing.setOpenTime(park.getOpenTime());
                if (park.getCloseTime() != null) existing.setCloseTime(park.getCloseTime());
                if (park.getEventNote() != null) existing.setEventNote(park.getEventNote());
                if (park.getStatus() != null) existing.setStatus(park.getStatus());
                return parkRepository.save(existing);
            }
        }
        return parkRepository.save(park);
    }

}

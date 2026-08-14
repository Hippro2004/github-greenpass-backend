package com.example.greenpass.v1.Stamp.services;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.greenpass.v1.Park.entities.Park;
import com.example.greenpass.v1.ParkRanger.entities.ParkRanger;
import com.example.greenpass.v1.ParkRanger.services.ParkRangerService;
import com.example.greenpass.v1.Stamp.entities.Stamp;
import com.example.greenpass.v1.Stamp.repositories.StampRepository;
import com.example.greenpass.v1.User.entities.User;
import com.example.greenpass.v1.User.services.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StampServiceImpl implements StampService {
    private final UserService userService;
    private final ParkRangerService parkRangerService;
    private final StampRepository stampRepository;

    @Override
    public List<Stamp> getAllStampsByUsername(String username) {
        return stampRepository.findAllByUserUsername(username);
    }

    @Override
    public Stamp getStampById(int id) {
        return stampRepository.findById(id).orElseThrow();
    }

    @Override
    public void StampUser(String username, String parkrangerUsername) {
        User user = userService.getUserByUsername(username);
        ParkRanger parkRanger = parkRangerService.getParkRangerByUsername(parkrangerUsername);
        Park park = parkRanger.getPark();

        Stamp newStamp = Stamp.builder()
                .stampDate(LocalDate.now())
                .user(user)
                .park(park)
                .build();

        stampRepository.save(newStamp);
    }

    @Override
    public List<Stamp> getAllStampsByUsernameAndParkId(String username, int parkId) {
        return stampRepository.findAllByUserUsernameAndParkParkId(username, parkId);
    }

}

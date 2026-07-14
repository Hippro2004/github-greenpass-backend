package com.example.greenpass.v1.services;

import java.util.List;

import com.example.greenpass.v1.entities.Stamp;

public interface StampService {
    List<Stamp> getAllStampsByUsername(String username);

    Stamp getStampById(int id);

    void StampUser(String username, String parkRangerUsername);

    List<Stamp> getAllStampsByUsernameAndParkId(String username, int parkId);
}

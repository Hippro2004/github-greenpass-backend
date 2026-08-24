package com.example.greenpass.v1.Stamp.services;

import java.util.List;

import com.example.greenpass.v1.Stamp.dtos.VisitStatisticsResponse;
import com.example.greenpass.v1.Stamp.entities.Stamp;

public interface StampService {
    List<Stamp> getAllStampsByUsername(String username);

    Stamp getStampById(int id);

    void StampUser(String username, String parkRangerUsername);

    List<Stamp> getAllStampsByUsernameAndParkId(String username, int parkId);

    boolean hasUserBeenStampedToday(String username, Integer parkId);

    VisitStatisticsResponse getVisitStatistics();
}

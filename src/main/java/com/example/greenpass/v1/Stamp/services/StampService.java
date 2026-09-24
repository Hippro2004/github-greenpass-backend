package com.example.greenpass.v1.Stamp.services;

import java.util.List;

import com.example.greenpass.v1.Stamp.dtos.StampResponse;
import com.example.greenpass.v1.Stamp.dtos.VisitStatisticsResponse;
import com.example.greenpass.v1.Stamp.entities.Stamp;

public interface StampService {
    List<StampResponse> getAllStampsByUsername(String username);

    Stamp getStampById(int id);

    void stampUser(String username, String parkRangerUsername);

    List<Stamp> getAllStampsByUsernameAndParkId(String username, int parkId);

    boolean hasUserBeenStampedToday(String username, Integer parkId);

    boolean hasUserBeenStampedWithinHours(String username, Integer parkId, int hours);

    VisitStatisticsResponse getVisitStatistics();

    VisitStatisticsResponse getVisitStatistics(Integer parkId, String username);
}

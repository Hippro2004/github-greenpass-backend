package com.example.greenpass.v1.Reward.services;

import java.util.List;

import com.example.greenpass.v1.Reward.entities.Reward;

public interface RewardService {

    List<Reward> getAllReward();

    Reward getRewardById(int id);

}

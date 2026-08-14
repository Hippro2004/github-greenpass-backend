package com.example.greenpass.v1.Reward.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.greenpass.v1.Reward.entities.Reward;
import com.example.greenpass.v1.Reward.repositories.RewardRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RewardServiceImpl implements RewardService {
    private final RewardRepository rewardRepository;

    @Override
    public List<Reward> getAllReward() {
        return rewardRepository.findAll();
    }

    @Override
    public Reward getRewardById(int id) {
        return rewardRepository.findByRewardId(id).orElse(null);
    }

}

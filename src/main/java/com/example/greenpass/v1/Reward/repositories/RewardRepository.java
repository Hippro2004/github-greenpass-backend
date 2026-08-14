package com.example.greenpass.v1.Reward.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.greenpass.v1.Reward.entities.Reward;

public interface RewardRepository extends JpaRepository<Reward, Integer> {
    Reward findByRewardId(int id);

}

package com.example.greenpass.v1.Reward.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.greenpass.v1.Reward.entities.Reward;

public interface RewardRepository extends JpaRepository<Reward, Integer> {
    Optional<Reward> findByRewardId(int id);

}

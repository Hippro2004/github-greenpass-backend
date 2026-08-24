package com.example.greenpass.v1.Reward.services;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.greenpass.v1.Reward.dtos.AddRewardDto;
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

    @Override
    public Reward addReward(AddRewardDto dto) {
        Reward reward = Reward.builder()
                .rewardTitle(dto.getRewardTitle())
                .rewardDetails(dto.getRewardDetails())
                .rewardAnnouncementDate(LocalDate.now())
                .image(dto.getImage())
                .build();
        return rewardRepository.save(reward);
    }

    @Override
    public Reward updateReward(int id, AddRewardDto dto) {
        Reward reward = rewardRepository.findById(id).orElse(null);
        reward.setRewardTitle(dto.getRewardTitle());
        reward.setRewardDetails(dto.getRewardDetails());
        if (dto.getImage() != null && !dto.getImage().trim().isEmpty()) {
            reward.setImage(dto.getImage());
        }

        return rewardRepository.save(reward);
    }

}

package com.example.greenpass.v1.Reward.services;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.greenpass.v1.Reward.dtos.AddRewardDto;
import com.example.greenpass.v1.Reward.entities.Reward;
import com.example.greenpass.v1.Reward.repositories.RewardRepository;
import com.example.greenpass.utils.FileUtils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RewardServiceImpl implements RewardService {
    private final RewardRepository rewardRepository;

    @Override
    public List<Reward> getAllReward() {
        return rewardRepository.findAll().stream().peek(r -> {
            r.setImage(FileUtils.extractFileName(r.getImage(), "rewards"));
        }).toList();
    }

    @Override
    public Reward getRewardById(int id) {
        Reward reward = rewardRepository.findByRewardId(id).orElse(null);
        if (reward != null) {
            reward.setImage(FileUtils.extractFileName(reward.getImage(), "rewards"));
        }
        return reward;
    }

    @Override
    public Reward addReward(AddRewardDto dto) {
        String cleanImage = FileUtils.extractFileName(dto.getImage(), "rewards");
        Reward reward = Reward.builder()
                .rewardTitle(dto.getRewardTitle())
                .rewardDetails(dto.getRewardDetails())
                .rewardAnnouncementDate(LocalDate.now())
                .image(cleanImage)
                .build();
        return rewardRepository.save(reward);
    }

    @Override
    public Reward updateReward(int id, AddRewardDto dto) {
        Reward reward = rewardRepository.findById(id).orElse(null);
        if (reward == null) {
            return null;
        }
        reward.setRewardTitle(dto.getRewardTitle());
        reward.setRewardDetails(dto.getRewardDetails());
        if (dto.getImage() != null && !dto.getImage().trim().isEmpty()) {
            String newImage = FileUtils.extractFileName(dto.getImage(), "rewards");
            if (reward.getImage() != null && !reward.getImage().equals(newImage)) {
                FileUtils.deleteFile(reward.getImage(), "rewards");
            }
            reward.setImage(newImage);
        }

        return rewardRepository.save(reward);
    }

    @Override
    public void deleteReward(int id) {
        Reward reward = rewardRepository.findById(id).orElse(null);
        if (reward != null) {
            if (reward.getImage() != null) {
                FileUtils.deleteFile(reward.getImage(), "rewards");
            }
            rewardRepository.delete(reward);
        }
    }

}

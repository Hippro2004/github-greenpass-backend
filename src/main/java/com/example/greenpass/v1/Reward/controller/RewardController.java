package com.example.greenpass.v1.Reward.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.greenpass.dtos.ResponseObject;
import com.example.greenpass.v1.Reward.entities.Reward;
import com.example.greenpass.v1.Reward.services.RewardService;

import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/reward")
@RequiredArgsConstructor
public class RewardController {
    private final RewardService rewardService;

    @GetMapping("/reward-all")
    public ResponseEntity<ResponseObject> getAllReward() {
        try {
            List<Reward> rewards = rewardService.getAllReward();

            if (rewards != null && !rewards.isEmpty()) {
                return new ResponseEntity<>(new ResponseObject(true, "Reward found", rewards), HttpStatus.OK);

            }
            return new ResponseEntity<>(new ResponseObject(false, "Reward is not found", null), HttpStatus.NOT_FOUND);

        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseObject(false, "Failed to retrieve rewards", null),
                    HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }

    @GetMapping("/reward-id")
    public ResponseEntity<ResponseObject> getRewardById(@RequestParam int id) {
        try {
            Reward reward = rewardService.getRewardById(id);
            return new ResponseEntity<>(new ResponseObject(true, "Reward is found by id", reward), HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseObject(false, "Failed to retrieve reward by id ", null),
                    HttpStatus.INTERNAL_SERVER_ERROR);

        }

    }

}

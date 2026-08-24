package com.example.greenpass.v1.Reward.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.greenpass.dtos.ResponseObject;
import com.example.greenpass.v1.Reward.dtos.AddRewardDto;
import com.example.greenpass.v1.Reward.entities.Reward;
import com.example.greenpass.v1.Reward.services.RewardService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/reward")
@RequiredArgsConstructor
public class RewardController {
    private final RewardService rewardService;

    @GetMapping("/reward-all")
    public ResponseEntity<ResponseObject> getAllReward() {
        try {
            List<Reward> rewards = rewardService.getAllReward();

            if (rewards == null) {
                return new ResponseEntity<>(new ResponseObject(false, "Reward is not found", null),
                        HttpStatus.NOT_FOUND);

            }

            return new ResponseEntity<>(new ResponseObject(true, "Reward found", rewards), HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseObject(false, "Failed to retrieve rewards", null),
                    HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseObject> getRewardById(@PathVariable int id) {
        try {
            Reward reward = rewardService.getRewardById(id);
            if (reward == null) {
                return new ResponseEntity<>(new ResponseObject(false, "Reward not found", reward),
                        HttpStatus.NOT_FOUND);

            }
            return new ResponseEntity<>(new ResponseObject(true, "Reward found", reward), HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseObject(false, "Failed to retrieve reward by id ", null),
                    HttpStatus.INTERNAL_SERVER_ERROR);

        }

    }

    @PostMapping("/update")
    public ResponseEntity<ResponseObject> updateReward(@RequestParam("id") int id,
            @RequestBody @Valid AddRewardDto dto) {
        try {
            Reward updated = rewardService.updateReward(id, dto);
            return new ResponseEntity<>(new ResponseObject(true, "Update Reward Successfully", updated), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new ResponseObject(false, "Failed to Update Reward", null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}

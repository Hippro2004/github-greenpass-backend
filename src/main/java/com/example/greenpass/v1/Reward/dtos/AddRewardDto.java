package com.example.greenpass.v1.Reward.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddRewardDto {
    private String rewardTitle;
    private String rewardDetails;
    private String image;

}

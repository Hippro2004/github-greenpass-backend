package com.example.greenpass.v1.ParkRanger.services;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.greenpass.v1.Park.entities.Park;
import com.example.greenpass.v1.Park.services.ParkService;
import com.example.greenpass.v1.ParkRanger.dtos.AddParkRangerDto;
import com.example.greenpass.v1.ParkRanger.entities.ParkRanger;
import com.example.greenpass.v1.ParkRanger.repositories.ParkRangerRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ParkRangerServiceImpl implements ParkRangerService {

    private final ParkRangerRepository parkRangerRepository;
    private final ParkService parkService;

    @Override
    public ParkRanger getParkRangerByUsername(String username) {
        return parkRangerRepository.findByUsername(username);
    }

    @Override
    public ParkRanger addParkRanger(AddParkRangerDto dto) {
        Park park = parkService.getParkById(dto.getParkId());
        ParkRanger ranger = ParkRanger.builder()
                .username(dto.getUsername())
                .password(dto.getPassword())
                .firstname(dto.getFirstName())
                .surname(dto.getLastName())
                .mobilephone(dto.getPhone())
                .email(dto.getEmail())
                .birthDate(LocalDate.parse(dto.getBirthDate()))
                .gender(dto.getGender())
                .signature("src/sig1.png")
                .district(dto.getDistrict())
                .subDistrict(dto.getSubDistrict())
                .province(dto.getProvince())
                .zipcode(dto.getZipcode())
                .position(dto.getPosition())
                .startDate(LocalDate.parse(dto.getStartDate()))
                .canAnnouncement(false)
                .canIssueStamp(false)
                .canProgressReport(false)
                .canEditParkDetails(false)
                .park(park)
                .build();
        return parkRangerRepository.save(ranger);
    }

    @Override
    public List<ParkRanger> getAllParkRangers() {
        return parkRangerRepository.findAll();
    }

    @Override
    public ParkRanger updateParkRanger(String username, AddParkRangerDto dto) {
        ParkRanger parkRanger = parkRangerRepository.findByUsername(username);

        parkRanger.setPassword(dto.getPassword());
        parkRanger.setFirstname(dto.getFirstName());
        parkRanger.setSurname(dto.getLastName());
        parkRanger.setMobilephone(dto.getPhone());
        parkRanger.setEmail(dto.getEmail());
        parkRanger.setBirthDate(LocalDate.parse(dto.getBirthDate()));
        parkRanger.setGender(dto.getGender());
        parkRanger.setDistrict(dto.getDistrict());
        parkRanger.setSubDistrict(dto.getSubDistrict());
        parkRanger.setProvince(dto.getProvince());
        parkRanger.setZipcode(dto.getZipcode());
        parkRanger.setPosition(dto.getPosition());
        parkRanger.setStartDate(LocalDate.parse(dto.getStartDate()));

        return parkRangerRepository.save(parkRanger);
    }

}

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
        int targetParkId = (dto.getParkId() != null && dto.getParkId() > 0) ? dto.getParkId() : 1;
        Park park = null;
        try {
            park = parkService.getParkById(targetParkId);
        } catch (Exception e) {
            try {
                park = parkService.getParkById(1);
            } catch (Exception ex) {}
        }

        String username = safeTruncate(dto.getUsername(), 50, "pr" + System.currentTimeMillis());
        String password = safeTruncate(dto.getPassword(), 16, "pass1234");
        String firstname = safeTruncate(dto.getFirstName(), 50, "เจ้าหน้าที่");
        String surname = safeTruncate(dto.getLastName(), 50, "อุทยาน");

        String rawPhone = dto.getPhone() != null ? dto.getPhone().replaceAll("[^0-9]", "") : "0890000000";
        String mobilephone = safeTruncate(rawPhone, 10, "0890000000");

        String email = safeTruncate(dto.getEmail(), 50, "ranger@park.go.th");
        String district = safeTruncate(dto.getDistrict(), 20, "เมือง");
        String subDistrict = safeTruncate(dto.getSubDistrict(), 20, "ในเมือง");
        String province = safeTruncate(dto.getProvince(), 20, "ฉะเชิงเทรา");
        String zipcode = safeTruncate(dto.getZipcode(), 5, "10000");
        String position = safeTruncate(dto.getPosition(), 25, "เจ้าหน้าที่อุทยาน");

        LocalDate parsedBirthDate = LocalDate.of(2000, 1, 1);
        if (dto.getBirthDate() != null && !dto.getBirthDate().trim().isEmpty()) {
            try {
                parsedBirthDate = LocalDate.parse(dto.getBirthDate().trim());
            } catch (Exception e) {}
        }

        LocalDate parsedStartDate = LocalDate.now();
        if (dto.getStartDate() != null && !dto.getStartDate().trim().isEmpty()) {
            try {
                parsedStartDate = LocalDate.parse(dto.getStartDate().trim());
            } catch (Exception e) {}
        }

        ParkRanger ranger = ParkRanger.builder()
                .username(username)
                .password(password)
                .firstname(firstname)
                .surname(surname)
                .mobilephone(mobilephone)
                .email(email)
                .birthDate(parsedBirthDate)
                .gender(dto.getGender() > 0 ? dto.getGender() : 1)
                .signature("src/sig1.png")
                .district(district)
                .subDistrict(subDistrict)
                .province(province)
                .zipcode(zipcode)
                .position(position)
                .startDate(parsedStartDate)
                .canAnnouncement(true)
                .canIssueStamp(true)
                .canProgressReport(true)
                .canEditParkDetails(true)
                .park(park)
                .build();
        return parkRangerRepository.save(ranger);
    }

    private String safeTruncate(String val, int maxLen, String defaultVal) {
        if (val == null || val.trim().isEmpty()) return defaultVal;
        String trimmed = val.trim();
        return trimmed.length() > maxLen ? trimmed.substring(0, maxLen) : trimmed;
    }

    @Override
    public List<ParkRanger> getAllParkRangers() {
        return parkRangerRepository.findAll();
    }

    @Override
    public ParkRanger updateParkRanger(String username, AddParkRangerDto dto) {
        ParkRanger parkRanger = parkRangerRepository.findByUsername(username);
        if (parkRanger == null) return null;

        if (dto.getPassword() != null) parkRanger.setPassword(safeTruncate(dto.getPassword(), 16, parkRanger.getPassword()));
        if (dto.getFirstName() != null) parkRanger.setFirstname(safeTruncate(dto.getFirstName(), 50, parkRanger.getFirstname()));
        if (dto.getLastName() != null) parkRanger.setSurname(safeTruncate(dto.getLastName(), 50, parkRanger.getSurname()));
        if (dto.getPhone() != null) {
            String rawPhone = dto.getPhone().replaceAll("[^0-9]", "");
            parkRanger.setMobilephone(safeTruncate(rawPhone, 10, parkRanger.getMobilephone()));
        }
        if (dto.getEmail() != null) parkRanger.setEmail(safeTruncate(dto.getEmail(), 50, parkRanger.getEmail()));
        if (dto.getDistrict() != null) parkRanger.setDistrict(safeTruncate(dto.getDistrict(), 20, parkRanger.getDistrict()));
        if (dto.getSubDistrict() != null) parkRanger.setSubDistrict(safeTruncate(dto.getSubDistrict(), 20, parkRanger.getSubDistrict()));
        if (dto.getProvince() != null) parkRanger.setProvince(safeTruncate(dto.getProvince(), 20, parkRanger.getProvince()));
        if (dto.getZipcode() != null) parkRanger.setZipcode(safeTruncate(dto.getZipcode(), 5, parkRanger.getZipcode()));
        if (dto.getPosition() != null) parkRanger.setPosition(safeTruncate(dto.getPosition(), 25, parkRanger.getPosition()));

        if (dto.getBirthDate() != null && !dto.getBirthDate().trim().isEmpty()) {
            try {
                parkRanger.setBirthDate(LocalDate.parse(dto.getBirthDate().trim()));
            } catch (Exception e) {}
        }
        if (dto.getStartDate() != null && !dto.getStartDate().trim().isEmpty()) {
            try {
                parkRanger.setStartDate(LocalDate.parse(dto.getStartDate().trim()));
            } catch (Exception e) {}
        }
        if (dto.getGender() != null && dto.getGender() > 0) {
            parkRanger.setGender(dto.getGender());
        }

        if (dto.getCanAnnouncement() != null) parkRanger.setCanAnnouncement(dto.getCanAnnouncement());
        if (dto.getCanIssueStamp() != null) parkRanger.setCanIssueStamp(dto.getCanIssueStamp());
        if (dto.getCanProgressReport() != null) parkRanger.setCanProgressReport(dto.getCanProgressReport());
        if (dto.getCanEditParkDetails() != null) parkRanger.setCanEditParkDetails(dto.getCanEditParkDetails());

        return parkRangerRepository.save(parkRanger);
    }

}

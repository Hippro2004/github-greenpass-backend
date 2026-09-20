package com.example.greenpass.v1.User.services;

import org.springframework.stereotype.Service;

import com.example.greenpass.v1.User.dtos.RegisterUserDto;
import com.example.greenpass.v1.User.dtos.UpdateUserDto;
import com.example.greenpass.v1.User.entities.User;
import com.example.greenpass.v1.User.repositories.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserSerivceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }

    @Override
    public UpdateUserDto editProfileUserDto(String username) {
        User user = getUserByUsername(username);
        if (user == null) {
            return null;
        }
        UpdateUserDto updateUserDto = new UpdateUserDto();
        updateUserDto.setFirstname(user.getFirstname());
        updateUserDto.setLastname(user.getLastname());
        updateUserDto.setEmail(user.getEmail());
        updateUserDto.setPhone(user.getPhone());
        updateUserDto.setBirthDate(user.getBirthDate());
        updateUserDto.setGender(user.getGender());
        updateUserDto.setForeigner(user.isForeigner());
        updateUserDto.setDistrict(user.getDistrict());
        updateUserDto.setSubDistrict(user.getSubDistrict());
        updateUserDto.setProvince(user.getProvince());
        updateUserDto.setZipcode(user.getZipcode());
        updateUserDto.setProfileImage(user.getProfileImage());
        return updateUserDto;
    }

    @Override
    public void registerUser(RegisterUserDto registerUserDto) {
        User toSaveUser = User.builder()
                .username(registerUserDto.getUsername())
                .password(registerUserDto.getPassword())
                .firstname(registerUserDto.getFirstname())
                .lastname(registerUserDto.getLastname())
                .phone(registerUserDto.getPhone())
                .email(registerUserDto.getEmail())
                .birthDate(registerUserDto.getBirthDate())
                .gender(registerUserDto.getGender())
                .district(registerUserDto.getDistrict())
                .isForeigner(registerUserDto.isForeigner())
                .subDistrict(registerUserDto.getSubDistrict())
                .province(registerUserDto.getProvince())
                .zipcode(registerUserDto.getZipcode())
                .build();
        userRepository.save(toSaveUser);

    }

    @Override
    public void updateUser(String username, UpdateUserDto dto) {
        User user = getUserByUsername(username);
        user.setFirstname(dto.getFirstname());
        user.setLastname(dto.getLastname());
        user.setProfileImage(dto.getProfileImage());
        user.setEmail(dto.getEmail());
        user.setPhone(dto.getPhone());
        user.setBirthDate(dto.getBirthDate());
        user.setGender(dto.getGender());
        user.setForeigner(dto.isForeigner());
        user.setDistrict(dto.getDistrict());
        user.setSubDistrict(dto.getSubDistrict());
        user.setProvince(dto.getProvince());
        user.setZipcode(dto.getZipcode());
        if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
            user.setPassword(dto.getPassword());
        }
        userRepository.save(user);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

}
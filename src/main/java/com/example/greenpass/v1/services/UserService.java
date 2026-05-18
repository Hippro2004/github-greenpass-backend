package com.example.greenpass.v1.services;

import com.example.greenpass.v1.dtos.RegisterUserDto;
import com.example.greenpass.v1.dtos.UpdateUserDto;
import com.example.greenpass.v1.entities.User;

public interface UserService {
    void registerUser(RegisterUserDto registerUserDto);

    User getUserByUsername(String username);

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);

    void updateUser(String username, UpdateUserDto updateUserDto);
}

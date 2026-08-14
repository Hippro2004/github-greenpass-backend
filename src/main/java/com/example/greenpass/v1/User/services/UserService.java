package com.example.greenpass.v1.User.services;

import com.example.greenpass.v1.User.dtos.RegisterUserDto;
import com.example.greenpass.v1.User.dtos.UpdateUserDto;
import com.example.greenpass.v1.User.entities.User;

public interface UserService {
    void registerUser(RegisterUserDto registerUserDto);

    User getUserByUsername(String username);

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);

    void updateUser(String username, UpdateUserDto updateUserDto);
}

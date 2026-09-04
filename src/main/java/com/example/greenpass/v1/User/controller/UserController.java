package com.example.greenpass.v1.User.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.greenpass.dtos.ResponseObject;
import com.example.greenpass.v1.User.dtos.LoginUserDto;
import com.example.greenpass.v1.User.dtos.RegisterUserDto;
import com.example.greenpass.v1.User.dtos.UpdateUserDto;
import com.example.greenpass.v1.User.entities.User;
import com.example.greenpass.v1.User.services.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<ResponseObject> register(@RequestBody @Valid RegisterUserDto registerUserDto) {
        try {
            boolean existsUsername = userService.existsByUsername(registerUserDto.getUsername());
            boolean existsEmail = userService.existsByEmail(registerUserDto.getEmail());

            if (existsUsername) {
                return new ResponseEntity<>(new ResponseObject(false, "Username already exists", null),
                        HttpStatus.CONFLICT);

            }

            if (existsEmail) {
                return new ResponseEntity<>(new ResponseObject(false, "Email already exists", null),
                        HttpStatus.CONFLICT);
            }

            userService.registerUser(registerUserDto);
            return new ResponseEntity<>(new ResponseObject(true, "Create User Successfully", null),
                    HttpStatus.CREATED);

        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseObject(false, "Failed to create user", null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PostMapping("/login")
    public ResponseEntity<ResponseObject> login(@RequestBody @Valid LoginUserDto loginUserDto) {
        try {
            User user = userService.getUserByUsername(loginUserDto.getUsername());
            if (user != null) {
                if (user.getPassword().equals(loginUserDto.getPassword())) {
                    return new ResponseEntity<>(new ResponseObject(true, "User Login Successfully", user),
                            HttpStatus.OK);
                }
                return new ResponseEntity<>(new ResponseObject(false, "Password incorrect", null),
                        HttpStatus.UNAUTHORIZED);
            }
            return new ResponseEntity<>(new ResponseObject(false, "User not found", null),
                    HttpStatus.NOT_FOUND);

        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseObject(false, "Failed to Login",
                    null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{username}")
    public ResponseEntity<ResponseObject> updateUser(
            @PathVariable("username") String username,
            @RequestBody UpdateUserDto updateUserDto) {
        try {
            User user = userService.getUserByUsername(username);
            if (user == null) {
                return new ResponseEntity<>(
                        new ResponseObject(false, "User not found", null),
                        HttpStatus.NOT_FOUND);
            }
            userService.updateUser(user.getUsername(), updateUserDto);
            return new ResponseEntity<>(
                    new ResponseObject(true, "Update successfully", null),
                    HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(
                    new ResponseObject(false, "Failed to update", null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{username}/fcm-token")
    public ResponseEntity<ResponseObject> updateFcmToken(
            @PathVariable("username") String username,
            @RequestBody String fcmToken) {
        try {
            User user = userService.getUserByUsername(username);
            if (user == null) {
                return new ResponseEntity<>(
                        new ResponseObject(false, "User not found", null),
                        HttpStatus.NOT_FOUND);
            }
            userService.updateFcmToken(user.getUsername(), fcmToken);
            return new ResponseEntity<>(
                    new ResponseObject(true, "Update successfully", null),
                    HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(
                    new ResponseObject(false, "Failed to update", null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}

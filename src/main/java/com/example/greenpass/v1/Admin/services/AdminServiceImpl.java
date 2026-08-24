package com.example.greenpass.v1.Admin.services;

import org.springframework.stereotype.Service;

import com.example.greenpass.v1.Admin.entities.Admin;
import com.example.greenpass.v1.Admin.repositories.AdminRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final AdminRepository adminRepository;

    @Override
    public Admin getAdmin(String username) {
        return adminRepository.findById(username).orElse(null);

    }

}

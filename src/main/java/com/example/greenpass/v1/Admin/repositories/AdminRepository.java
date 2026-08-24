package com.example.greenpass.v1.Admin.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.greenpass.v1.Admin.entities.Admin;

public interface AdminRepository extends JpaRepository<Admin, String> {
}

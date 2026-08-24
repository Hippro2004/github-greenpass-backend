package com.example.greenpass.v1.Admin.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "admin")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Admin {

    @Id
    @Column(nullable = false, length = 50)
    private String username;

    @Column(nullable = false, length = 32)
    private String password;
}

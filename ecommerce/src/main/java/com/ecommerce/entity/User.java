package com.ecommerce.entity;

import jakarta.persistence.*;
import lombok.Data;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

    @Entity
    @Data
    @Table(name = "users")
    public class User {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @NotBlank(message = "Name is required")
        @Column(nullable = false)
        private String name;

        @Email(message = "Invalid email format")
        @NotBlank(message = "Email is required")
        @Column(nullable = false, unique = true)
        private String email;

        @NotBlank(message = "Password is required")
        @Size(min = 6, message = "Password must be at least 6 characters")
        @Column(nullable = false)
        private String password;

        @Column(nullable = false)
        private String role;
    }





package com.example.LibraryManagementSystem.entity;

import javax.persistence.*;
import com.example.LibraryManagementSystem.enums.Role;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    @Enumerated(EnumType.STRING)
    private Role role;
}

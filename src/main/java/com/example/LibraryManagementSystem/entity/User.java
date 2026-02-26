package com.example.LibraryManagementSystem.entity;

import javax.persistence.*;
import com.example.LibraryManagementSystem.enums.Role;
import lombok.Data;

@Entity
@Data
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;
}

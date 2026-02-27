package com.example.LibraryManagementSystem.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Data
public class Borrowing {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate borrowDate;
    private LocalDate dueDate;

    private boolean returned = false;

    @ManyToOne
    private User user;

    @ManyToOne
    private Book book;
}

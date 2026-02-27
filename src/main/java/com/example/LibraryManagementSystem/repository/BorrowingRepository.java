package com.example.LibraryManagementSystem.repository;

import com.example.LibraryManagementSystem.entity.Borrowing;
import com.example.LibraryManagementSystem.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BorrowingRepository extends JpaRepository<Borrowing, Long> {

    long countByUserAndReturnedFalse(User user);
}
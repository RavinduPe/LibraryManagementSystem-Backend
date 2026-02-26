package com.example.LibraryManagementSystem.controller;

import com.example.LibraryManagementSystem.dto.BorrowingDto;
import com.example.LibraryManagementSystem.service.BorrowingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/borrow")
@CrossOrigin(origins = "http://localhost:5173")
public class BorrowingController {

    @Autowired
    private BorrowingService borrowingService;

    // USER or ADMIN can borrow
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @PostMapping("/{userId}/{bookId}")
    public ResponseEntity<BorrowingDto> borrowBook(@PathVariable Long userId,
                                                   @PathVariable Long bookId) {

        BorrowingDto dto = borrowingService.borrowBook(userId, bookId);
        return ResponseEntity.ok(dto);
    }

    // USER or ADMIN can return
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @PutMapping("/return/{borrowingId}")
    public ResponseEntity<BorrowingDto> returnBook(@PathVariable Long borrowingId) {

        BorrowingDto dto = borrowingService.returnBook(borrowingId);
        return ResponseEntity.ok(dto);
    }
}
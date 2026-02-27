package com.example.LibraryManagementSystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class BorrowingDto {

    private Long id; // auto-generated, so no validation

    @NotNull(message = "User ID is required")
    private Long userId;

    @NotNull(message = "Username is required")
    private String username;

    @NotNull(message = "Book ID is required")
    private Long bookId;

    @NotNull(message = "Book title is required")
    private String bookTitle;

    @NotNull(message = "Borrow date is required")
    private LocalDate borrowDate;

    @NotNull(message = "Due date is required")
    private LocalDate dueDate;

    @NotNull(message = "Returned status is required")
    private Boolean returned;
}
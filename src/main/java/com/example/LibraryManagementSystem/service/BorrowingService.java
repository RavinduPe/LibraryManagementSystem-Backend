package com.example.LibraryManagementSystem.service;

import com.example.LibraryManagementSystem.dto.BorrowingDto;
import com.example.LibraryManagementSystem.entity.Book;
import com.example.LibraryManagementSystem.entity.Borrowing;
import com.example.LibraryManagementSystem.entity.User;
import com.example.LibraryManagementSystem.repository.BookRepository;
import com.example.LibraryManagementSystem.repository.BorrowingRepository;
import com.example.LibraryManagementSystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class BorrowingService {

    @Autowired
    private BorrowingRepository borrowingRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private UserRepository userRepository;

    public BorrowingDto borrowBook(Long userId, Long bookId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found"));

        // Rule 1: Max 5 books
        long activeBorrowCount = borrowingRepository.countByUserAndReturnedFalse(user);
        if (activeBorrowCount >= 5) {
            throw new RuntimeException("User cannot borrow more than 5 books");
        }

        // Rule 2: Book availability
        if (book.getAvailableCopies() <= 0) {
            throw new RuntimeException("Book not available");
        }

        // Create borrowing
        Borrowing borrowing = new Borrowing();
        borrowing.setUser(user);
        borrowing.setBook(book);
        borrowing.setBorrowDate(LocalDate.now());
        borrowing.setDueDate(LocalDate.now().plusDays(7));
        borrowing.setReturned(false);

        borrowingRepository.save(borrowing);

        // Decrease available copies
        book.setAvailableCopies(book.getAvailableCopies() - 1);
        bookRepository.save(book);

        // Return DTO
        BorrowingDto dto = new BorrowingDto();
        dto.setId(borrowing.getId());
        dto.setUserId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setBookId(book.getId());
        dto.setBookTitle(book.getTitle());
        dto.setBorrowDate(borrowing.getBorrowDate());
        dto.setDueDate(borrowing.getDueDate());
        dto.setReturned(borrowing.isReturned());

        return dto;
    }

    public BorrowingDto returnBook(Long borrowingId) {

        Borrowing borrowing = borrowingRepository.findById(borrowingId)
                .orElseThrow(() -> new RuntimeException("Borrow record not found"));

        if (borrowing.isReturned()) {
            throw new RuntimeException("Book already returned");
        }

        borrowing.setReturned(true);
        borrowingRepository.save(borrowing);

        // Increase available copies
        Book book = borrowing.getBook();
        book.setAvailableCopies(book.getAvailableCopies() + 1);
        bookRepository.save(book);

        // Return DTO
        BorrowingDto dto = new BorrowingDto();
        dto.setId(borrowing.getId());
        dto.setUserId(borrowing.getUser().getId());
        dto.setUsername(borrowing.getUser().getUsername());
        dto.setBookId(book.getId());
        dto.setBookTitle(book.getTitle());
        dto.setBorrowDate(borrowing.getBorrowDate());
        dto.setDueDate(borrowing.getDueDate());
        dto.setReturned(borrowing.isReturned());

        return dto;
    }
}
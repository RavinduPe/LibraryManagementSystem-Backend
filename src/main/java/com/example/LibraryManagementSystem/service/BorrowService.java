package com.example.LibraryManagementSystem.service;

import com.example.LibraryManagementSystem.dto.BorrowDTO;
import com.example.LibraryManagementSystem.dto.BorrowRequestDTO;
import com.example.LibraryManagementSystem.entity.Book;
import com.example.LibraryManagementSystem.entity.Borrow;
import com.example.LibraryManagementSystem.entity.User;
import com.example.LibraryManagementSystem.repository.BookRepository;
import com.example.LibraryManagementSystem.repository.BorrowRepository;
import com.example.LibraryManagementSystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BorrowService {

    @Autowired
    private BorrowRepository borrowRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private UserService userService;

    @Transactional
    public BorrowDTO borrowBook(BorrowRequestDTO borrowRequest) {
        try {
            User currentUser = userService.getCurrentUser();
            Book book = bookRepository.findById(borrowRequest.getBookId())
                    .orElseThrow(() -> new RuntimeException("Book not found with id: " + borrowRequest.getBookId()));

            // Check if book is available
            if (!book.isAvailable()) {
                throw new RuntimeException("Book is not available for borrowing");
            }

            // Check if user already has this book borrowed and not returned
            if (borrowRepository.existsByUserAndBookAndReturnedFalse(currentUser, book)) {
                throw new RuntimeException("You have already borrowed this book and haven't returned it yet");
            }

            // Create borrow record
            Borrow borrow = new Borrow();
            borrow.setUser(currentUser);
            borrow.setBook(book);
            borrow.setBorrowDate(LocalDate.now());
            borrow.setReturned(false);

            // Update book availability
            book.setAvailable(false);
            bookRepository.save(book);

            Borrow savedBorrow = borrowRepository.save(borrow);
            return convertToDTO(savedBorrow);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error borrowing book: " + e.getMessage());
        }
    }

    @Transactional
    public BorrowDTO returnBook(Long borrowId) {
        try {
            Borrow borrow = borrowRepository.findById(borrowId)
                    .orElseThrow(() -> new RuntimeException("Borrow record not found with id: " + borrowId));

            // Check if already returned
            if (borrow.isReturned()) {
                throw new RuntimeException("Book has already been returned");
            }

            // Update borrow record
            borrow.setReturned(true);
            borrow.setReturnDate(LocalDate.now());

            // Update book availability
            Book book = borrow.getBook();
            book.setAvailable(true);
            bookRepository.save(book);

            Borrow updatedBorrow = borrowRepository.save(borrow);
            return convertToDTO(updatedBorrow);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error returning book: " + e.getMessage());
        }
    }

    public List<BorrowDTO> getCurrentUserBorrows() {
        try {
            User currentUser = userService.getCurrentUser();
            return borrowRepository.findByUser(currentUser)
                    .stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error getting user borrows: " + e.getMessage());
        }
    }

    public List<BorrowDTO> getAllBorrows() {
        try {
            return borrowRepository.findAll()
                    .stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error getting all borrows: " + e.getMessage());
        }
    }

    public List<BorrowDTO> getActiveBorrows() {
        try {
            return borrowRepository.findByReturnedFalse()
                    .stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error getting active borrows: " + e.getMessage());
        }
    }

    private BorrowDTO convertToDTO(Borrow borrow) {
        BorrowDTO dto = new BorrowDTO();
        dto.setId(borrow.getId());
        dto.setBorrowDate(borrow.getBorrowDate());
        dto.setReturnDate(borrow.getReturnDate());
        dto.setReturned(borrow.isReturned());

        // Set user info (only id and username)
        if (borrow.getUser() != null) {
            dto.setUserId(borrow.getUser().getId());
            dto.setUsername(borrow.getUser().getUsername());
        }

        // Set book info (only necessary fields)
        if (borrow.getBook() != null) {
            dto.setBookId(borrow.getBook().getId());
            dto.setBookTitle(borrow.getBook().getTitle());
            if (borrow.getBook().getAuthor() != null) {
                dto.setAuthorName(borrow.getBook().getAuthor().getName());
            }
        }

        return dto;
    }
}
package com.example.LibraryManagementSystem.controller;

import com.example.LibraryManagementSystem.dto.BookDto;
import com.example.LibraryManagementSystem.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@ResponseBody
@RequestMapping(value = "api/books")
public class BookController {

    @Autowired
    private BookService bookService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping()
    public ResponseEntity< BookDto > saveBook(@Valid @RequestBody BookDto bookDto)
    {
        BookDto response = bookService.saveBook(bookDto);

        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping()
    public ResponseEntity<List<BookDto>> getAllBook(){
        List<BookDto> books = bookService.getAllBooks();

        return ResponseEntity.ok(books);
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping("{bookId}")
    public ResponseEntity<BookDto> getBookById(@PathVariable String bookId){
        BookDto book = bookService.getBookById(bookId);
        return ResponseEntity.ok(book);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("{bookId}")
    public ResponseEntity<String> deleteBookById(@PathVariable String bookId)
    {
        String confirmResponse = bookService.deleteById(bookId);
        return ResponseEntity.ok(confirmResponse);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("{bookId}")
    public  ResponseEntity<String> updateBookById(@PathVariable String bookId , @RequestBody Map<String, String> requestBody)
    {
        String newTitle = requestBody.get("title");
        String updateResponse = bookService.updateBookTitle(bookId, newTitle);

        return ResponseEntity.ok(updateResponse);
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @PostMapping("/borrow")
    public ResponseEntity<String> barrowBook(@RequestParam Long memberId , @RequestParam Long bookId){
        bookService.borrowBook(memberId,bookId);
        return ResponseEntity.ok("Book borrowed successfully");
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @PostMapping("/return")
    public ResponseEntity<String> returnBook(@RequestParam Long memberId, @RequestParam Long bookId){
        bookService.returnBook(memberId,bookId);
        return ResponseEntity.ok("Book returned successfully");
    }
}

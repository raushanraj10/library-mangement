package com.example.library.controller;

import com.example.library.model.Book;
import com.example.library.model.BorrowRecord;
import com.example.library.model.User;
import com.example.library.service.BookService;
import com.example.library.service.BorrowService;
import com.example.library.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = "http://localhost:3000")
public class UserController {

    private final BookService bookService;
    private final BorrowService borrowService;
    private final UserService userService;

    public UserController(BookService bookService, BorrowService borrowService, UserService userService) {
        this.bookService = bookService;
        this.borrowService = borrowService;
        this.userService = userService;
    }

    // ---- Browse books (available copies shown, total copies not exposed here) ----

    @GetMapping("/books")
    public List<Book> getAllBooks() {
        return bookService.getAllBooks();
    }

    @GetMapping("/books/search")
    public List<Book> searchBooks(@RequestParam String keyword) {
        return bookService.searchBooks(keyword);
    }

    @GetMapping("/books/filter")
    public List<Book> filterBySubject(@RequestParam String subject) {
        return bookService.filterBySubject(subject);
    }

    // Powers the subject filter dropdown on the frontend.
    @GetMapping("/books/subjects")
    public List<String> getAllSubjects() {
        return bookService.getAllSubjects();
    }

    // ---- Borrow / return requests (both need admin approval) ----

    @PostMapping("/borrow-request/{bookId}/{userId}")
    public BorrowRecord requestBorrow(@PathVariable int bookId, @PathVariable int userId) {
        User user = userService.getUserById(userId);
        if (user == null) return null;
        return borrowService.requestBorrow(bookId, user);
    }

    @PostMapping("/return-request/{recordId}")
    public BorrowRecord requestReturn(@PathVariable int recordId) {
        return borrowService.requestReturn(recordId);
    }

    // "My Borrowed Books" page: every record for this user, with due dates,
    // overdue warnings and fines already calculated.
    @GetMapping("/my-books/{userId}")
    public List<BorrowRecord> getMyBooks(@PathVariable int userId) {
        return borrowService.getRecordsByUser(userId);
    }
}

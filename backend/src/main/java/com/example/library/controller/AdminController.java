package com.example.library.controller;

import com.example.library.model.Book;
import com.example.library.model.BookStats;
import com.example.library.model.BorrowRecord;
import com.example.library.model.User;
import com.example.library.service.BookService;
import com.example.library.service.BorrowService;
import com.example.library.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "http://localhost:3000")
public class AdminController {

    private final UserService userService;
    private final BookService bookService;
    private final BorrowService borrowService;

    public AdminController(UserService userService, BookService bookService, BorrowService borrowService) {
        this.userService = userService;
        this.bookService = bookService;
        this.borrowService = borrowService;
    }

    // ---- Manage users ----
    @GetMapping("/users")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @PostMapping("/users")
    public User addUser(@RequestBody User user) {
        return userService.addUser(user);
    }

    @DeleteMapping("/users/{id}")
    public void deleteUser(@PathVariable int id) {
        userService.deleteUser(id);
    }

    // ---- Manage books ----
    @GetMapping("/books")
    public List<Book> getAllBooks() {
        return bookService.getAllBooks();
    }

    @PostMapping("/books")
    public Book addBook(@RequestBody Book book) {
        return bookService.addBook(book);
    }

    @PutMapping("/books/{id}")
    public Book updateBook(@PathVariable int id, @RequestBody Book book) {
        return bookService.updateBook(id, book);
    }

    @DeleteMapping("/books/{id}")
    public void deleteBook(@PathVariable int id) {
        bookService.deleteBook(id);
    }

    // Admin-only dashboard numbers (total titles/copies/available/borrowed).
    // Never exposed through /api/user/... - the user side just doesn't call this.
    @GetMapping("/books/stats")
    public BookStats getBookStats() {
        return bookService.getStats();
    }

    // ---- Borrow / return requests ----

    // Requests waiting for approval (both new borrows and returns).
    @GetMapping("/requests")
    public List<BorrowRecord> getPendingRequests() {
        return borrowService.getPendingRequests();
    }

    // Full history of every borrow record, including overdue flags and fines.
    @GetMapping("/records")
    public List<BorrowRecord> getAllRecords() {
        return borrowService.getAllRecords();
    }

    @PutMapping("/requests/{id}/approve-borrow")
    public BorrowRecord approveBorrow(@PathVariable int id) {
        return borrowService.approveBorrow(id);
    }

    @PutMapping("/requests/{id}/reject-borrow")
    public BorrowRecord rejectBorrow(@PathVariable int id) {
        return borrowService.rejectBorrow(id);
    }

    @PutMapping("/requests/{id}/approve-return")
    public BorrowRecord approveReturn(@PathVariable int id) {
        return borrowService.approveReturn(id);
    }

    @PutMapping("/requests/{id}/reject-return")
    public BorrowRecord rejectReturn(@PathVariable int id) {
        return borrowService.rejectReturn(id);
    }
}

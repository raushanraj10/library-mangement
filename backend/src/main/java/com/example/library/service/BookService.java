package com.example.library.service;

import com.example.library.model.Book;
import com.example.library.model.BookStats;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class BookService {

    private final List<Book> books = new ArrayList<>();
    private final AtomicInteger idCounter = new AtomicInteger(1);

    public BookService() {
        books.add(new Book(idCounter.getAndIncrement(), "Clean Code", "Robert C. Martin", "Programming", 3));
        books.add(new Book(idCounter.getAndIncrement(), "Effective Java", "Joshua Bloch", "Programming", 2));
        books.add(new Book(idCounter.getAndIncrement(), "Sapiens", "Yuval Noah Harari", "History", 4));
    }

    public List<Book> getAllBooks() {
        return books;
    }

    public Book getBookById(int id) {
        for (Book b : books) {
            if (b.getId() == id) return b;
        }
        return null;
    }

    // Search by title or author (case-insensitive, partial match)
    public List<Book> searchBooks(String keyword) {
        List<Book> result = new ArrayList<>();
        String key = keyword.toLowerCase();
        for (Book b : books) {
            if (b.getTitle().toLowerCase().contains(key) || b.getAuthor().toLowerCase().contains(key)) {
                result.add(b);
            }
        }
        return result;
    }

    // Filter by exact subject (e.g. "Programming", "History")
    public List<Book> filterBySubject(String subject) {
        List<Book> result = new ArrayList<>();
        for (Book b : books) {
            if (b.getSubject().equalsIgnoreCase(subject)) result.add(b);
        }
        return result;
    }

    // Distinct subjects, used to build the filter dropdown on the frontend.
    public List<String> getAllSubjects() {
        Set<String> subjects = new LinkedHashSet<>();
        for (Book b : books) subjects.add(b.getSubject());
        return new ArrayList<>(subjects);
    }

    public Book addBook(Book book) {
        book.setId(idCounter.getAndIncrement());
        book.setAvailableCopies(book.getTotalCopies());
        books.add(book);
        return book;
    }

    public Book updateBook(int id, Book updated) {
        Book b = getBookById(id);
        if (b == null) return null;

        b.setTitle(updated.getTitle());
        b.setAuthor(updated.getAuthor());
        b.setSubject(updated.getSubject());

        // If total copies changed, shift available copies by the same amount.
        int diff = updated.getTotalCopies() - b.getTotalCopies();
        b.setTotalCopies(updated.getTotalCopies());
        b.setAvailableCopies(Math.max(0, b.getAvailableCopies() + diff));
        return b;
    }

    public boolean deleteBook(int id) {
        return books.removeIf(b -> b.getId() == id);
    }

    public void decreaseAvailableCopies(int id) {
        Book b = getBookById(id);
        if (b != null && b.getAvailableCopies() > 0) {
            b.setAvailableCopies(b.getAvailableCopies() - 1);
        }
    }

    public void increaseAvailableCopies(int id) {
        Book b = getBookById(id);
        if (b != null && b.getAvailableCopies() < b.getTotalCopies()) {
            b.setAvailableCopies(b.getAvailableCopies() + 1);
        }
    }

    // Admin-only counts (total titles/copies/available/borrowed) for the admin dashboard.
    public BookStats getStats() {
        int totalTitles = books.size();
        int totalCopies = 0;
        int totalAvailable = 0;
        for (Book b : books) {
            totalCopies += b.getTotalCopies();
            totalAvailable += b.getAvailableCopies();
        }
        int totalBorrowed = totalCopies - totalAvailable;
        return new BookStats(totalTitles, totalCopies, totalAvailable, totalBorrowed);
    }
}

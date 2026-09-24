package com.example.library.service;

import com.example.library.model.Book;
import com.example.library.model.BorrowRecord;
import com.example.library.model.User;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class BorrowService {

    private final List<BorrowRecord> records = new ArrayList<>();
    private final AtomicInteger idCounter = new AtomicInteger(1);

    private static final int BORROW_DAYS = 14;       // borrowing period once approved
    private static final double FINE_PER_DAY = 10.0; // fine charged per day late

    private final BookService bookService;

    public BorrowService(BookService bookService) {
        this.bookService = bookService;
    }

    // ---------- USER actions ----------

    // Step 1: user asks to borrow a book. Sits as PENDING_BORROW until admin acts on it.
    public BorrowRecord requestBorrow(int bookId, User user) {
        Book book = bookService.getBookById(bookId);
        if (book == null || book.getAvailableCopies() <= 0) return null;

        BorrowRecord record = new BorrowRecord();
        record.setId(idCounter.getAndIncrement());
        record.setBookId(book.getId());
        record.setBookTitle(book.getTitle());
        record.setBookAuthor(book.getAuthor());
        record.setUserId(user.getId());
        record.setUsername(user.getUsername());
        record.setStatus("PENDING_BORROW");
        record.setRequestDate(LocalDateTime.now());
        records.add(record);
        return record;
    }

    // Step 3: user asks to return a book they currently hold. Needs admin approval too.
    public BorrowRecord requestReturn(int recordId) {
        BorrowRecord record = getRecordById(recordId);
        if (record == null || !"BORROWED".equals(record.getStatus())) return null;

        record.setStatus("PENDING_RETURN");
        record.setReturnRequestDate(LocalDateTime.now());
        return record;
    }

    // ---------- ADMIN actions ----------

    // Step 2: admin approves the borrow -> stock goes down, due date is set (now + 14 days).
    public BorrowRecord approveBorrow(int recordId) {
        BorrowRecord record = getRecordById(recordId);
        if (record == null || !"PENDING_BORROW".equals(record.getStatus())) return null;

        Book book = bookService.getBookById(record.getBookId());
        if (book == null || book.getAvailableCopies() <= 0) return null;

        bookService.decreaseAvailableCopies(book.getId());
        record.setStatus("BORROWED");
        record.setApprovedDate(LocalDateTime.now());
        record.setDueDate(record.getApprovedDate().plusDays(BORROW_DAYS));
        return record;
    }

    public BorrowRecord rejectBorrow(int recordId) {
        BorrowRecord record = getRecordById(recordId);
        if (record == null || !"PENDING_BORROW".equals(record.getStatus())) return null;
        record.setStatus("REJECTED");
        return record;
    }

    // Step 4: admin approves the return -> stock goes back up, fine calculated if it was late.
    public BorrowRecord approveReturn(int recordId) {
        BorrowRecord record = getRecordById(recordId);
        if (record == null || !"PENDING_RETURN".equals(record.getStatus())) return null;

        record.setReturnDate(LocalDateTime.now());
        record.setStatus("RETURNED");

        if (record.getReturnDate().isAfter(record.getDueDate())) {
            long daysLate = Duration.between(record.getDueDate(), record.getReturnDate()).toDays();
            if (daysLate < 1) daysLate = 1; // any lateness at all still counts as a day
            record.setFine(daysLate * FINE_PER_DAY);
        }

        bookService.increaseAvailableCopies(record.getBookId());
        return record;
    }

    // Admin rejects the return request - the book simply stays borrowed by the user.
    public BorrowRecord rejectReturn(int recordId) {
        BorrowRecord record = getRecordById(recordId);
        if (record == null || !"PENDING_RETURN".equals(record.getStatus())) return null;
        record.setStatus("BORROWED");
        return record;
    }

    // ---------- Shared lookups ----------

    public BorrowRecord getRecordById(int id) {
        for (BorrowRecord r : records) {
            if (r.getId() == id) return r;
        }
        return null;
    }

    public List<BorrowRecord> getAllRecords() {
        recalcOverdue();
        return records;
    }

    // Requests waiting for admin action - shown on the Admin "Requests" page.
    public List<BorrowRecord> getPendingRequests() {
        List<BorrowRecord> pending = new ArrayList<>();
        for (BorrowRecord r : records) {
            if ("PENDING_BORROW".equals(r.getStatus()) || "PENDING_RETURN".equals(r.getStatus())) {
                pending.add(r);
            }
        }
        return pending;
    }

    // One user's full borrow history - shown on the "My Borrowed Books" page.
    public List<BorrowRecord> getRecordsByUser(int userId) {
        recalcOverdue();
        List<BorrowRecord> result = new ArrayList<>();
        for (BorrowRecord r : records) {
            if (r.getUserId() == userId) result.add(r);
        }
        return result;
    }

    // Recomputes the overdue flag + live estimated fine for every currently
    // borrowed book, based on right now. Called before any record list is returned.
    private void recalcOverdue() {
        LocalDateTime now = LocalDateTime.now();
        for (BorrowRecord r : records) {
            if ("BORROWED".equals(r.getStatus()) && r.getDueDate() != null && now.isAfter(r.getDueDate())) {
                r.setOverdue(true);
                long daysLate = Duration.between(r.getDueDate(), now).toDays();
                if (daysLate < 1) daysLate = 1;
                r.setEstimatedFine(daysLate * FINE_PER_DAY);
            } else {
                r.setOverdue(false);
                r.setEstimatedFine(0);
            }
        }
    }
}

package com.example.library.model;

import java.time.LocalDateTime;

// One record per borrow "lifecycle":
// PENDING_BORROW -> BORROWED -> PENDING_RETURN -> RETURNED
// (or REJECTED, if the admin rejects a borrow or return request)
public class BorrowRecord {
    private int id;
    private int bookId;
    private String bookTitle;
    private String bookAuthor;
    private int userId;
    private String username;

    private String status;

    private LocalDateTime requestDate;       // when the user asked to borrow
    private LocalDateTime approvedDate;      // when the admin approved the borrow
    private LocalDateTime dueDate;           // approvedDate + 14 days
    private LocalDateTime returnRequestDate; // when the user asked to return
    private LocalDateTime returnDate;        // when the admin approved the return

    private double fine;          // final fine, set once returned late
    private boolean overdue;      // true if still borrowed and past the due date
    private double estimatedFine; // live fine estimate while overdue and not yet returned

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getBookId() { return bookId; }
    public void setBookId(int bookId) { this.bookId = bookId; }

    public String getBookTitle() { return bookTitle; }
    public void setBookTitle(String bookTitle) { this.bookTitle = bookTitle; }

    public String getBookAuthor() { return bookAuthor; }
    public void setBookAuthor(String bookAuthor) { this.bookAuthor = bookAuthor; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getRequestDate() { return requestDate; }
    public void setRequestDate(LocalDateTime requestDate) { this.requestDate = requestDate; }

    public LocalDateTime getApprovedDate() { return approvedDate; }
    public void setApprovedDate(LocalDateTime approvedDate) { this.approvedDate = approvedDate; }

    public LocalDateTime getDueDate() { return dueDate; }
    public void setDueDate(LocalDateTime dueDate) { this.dueDate = dueDate; }

    public LocalDateTime getReturnRequestDate() { return returnRequestDate; }
    public void setReturnRequestDate(LocalDateTime returnRequestDate) { this.returnRequestDate = returnRequestDate; }

    public LocalDateTime getReturnDate() { return returnDate; }
    public void setReturnDate(LocalDateTime returnDate) { this.returnDate = returnDate; }

    public double getFine() { return fine; }
    public void setFine(double fine) { this.fine = fine; }

    public boolean isOverdue() { return overdue; }
    public void setOverdue(boolean overdue) { this.overdue = overdue; }

    public double getEstimatedFine() { return estimatedFine; }
    public void setEstimatedFine(double estimatedFine) { this.estimatedFine = estimatedFine; }
}

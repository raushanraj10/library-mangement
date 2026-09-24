package com.example.library.model;

// Simple holder for the admin dashboard's book counts.
// Not returned by any /api/user/... endpoint - user side never sees this.
public class BookStats {
    private int totalTitles;
    private int totalCopies;
    private int totalAvailable;
    private int totalBorrowed;

    public BookStats(int totalTitles, int totalCopies, int totalAvailable, int totalBorrowed) {
        this.totalTitles = totalTitles;
        this.totalCopies = totalCopies;
        this.totalAvailable = totalAvailable;
        this.totalBorrowed = totalBorrowed;
    }

    public int getTotalTitles() { return totalTitles; }
    public int getTotalCopies() { return totalCopies; }
    public int getTotalAvailable() { return totalAvailable; }
    public int getTotalBorrowed() { return totalBorrowed; }
}

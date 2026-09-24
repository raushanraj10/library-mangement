package com.example.library.model;

public class Book {
    private int id;
    private String title;
    private String author;
    private String subject;
    private int totalCopies;      // how many copies the library owns
    private int availableCopies;  // how many are free to borrow right now

    public Book() {}

    public Book(int id, String title, String author, String subject, int totalCopies) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.subject = subject;
        this.totalCopies = totalCopies;
        this.availableCopies = totalCopies;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }

    public int getTotalCopies() { return totalCopies; }
    public void setTotalCopies(int totalCopies) { this.totalCopies = totalCopies; }

    public int getAvailableCopies() { return availableCopies; }
    public void setAvailableCopies(int availableCopies) { this.availableCopies = availableCopies; }
}

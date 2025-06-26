package com.JEE.demo.entity;

import com.JEE.demo.entity.enums.BookStatus;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "books")
public class Book {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String author;

    @Column(nullable = false, unique = true)
    private String isbn;

    @Enumerated(EnumType.STRING)
    private BookStatus status;

    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "book")
    private List<Loan> loans = new ArrayList<>();

    public Book() {
        this.createdAt = LocalDateTime.now();
        this.status = BookStatus.AVAILABLE;
    }

    public Book(String title, String author, String isbn) {
        this();
        this.title = title;
        this.author = author;
        this.isbn = isbn;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }

    public BookStatus getStatus() { return status; }
    public void setStatus(BookStatus status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public List<Loan> getLoans() { return loans; }
    public void setLoans(List<Loan> loans) { this.loans = loans; }
}

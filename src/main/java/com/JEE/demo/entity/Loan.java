package com.JEE.demo.entity;

import com.JEE.demo.entity.enums.LoanStatus;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "loans")
public class Loan {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private User user;

    @ManyToOne(optional = false)
    private Book book;

    private LocalDateTime loanDate;
    private LocalDateTime returnDate;

    @Enumerated(EnumType.STRING)
    private LoanStatus status;

    public Loan() {}

    public Loan(User user, Book book) {
        this.user = user;
        this.book = book;
        this.loanDate = LocalDateTime.now();
        this.status = LoanStatus.ACTIVE;
    }

    public Long getId() { return id; }
    public User getUser() { return user; }
    public Book getBook() { return book; }
    public LocalDateTime getLoanDate() { return loanDate; }
    public LocalDateTime getReturnDate() { return returnDate; }
    public LoanStatus getStatus() { return status; }
    public void setReturnDate(LocalDateTime returnDate) { this.returnDate = returnDate; }
    public void setStatus(LoanStatus status) { this.status = status; }
}

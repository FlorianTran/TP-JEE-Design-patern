package com.JEE.demo.service;

import com.JEE.demo.dto.LoanDTO;
import com.JEE.demo.entity.*;
import com.JEE.demo.entity.enums.BookStatus;
import com.JEE.demo.entity.enums.LoanStatus;
import com.JEE.demo.factory.LoanFactory;
import com.JEE.demo.repository.BookRepository;
import com.JEE.demo.repository.LoanRepository;
import com.JEE.demo.repository.UserRepository;
import com.JEE.demo.service.observer.LoanObserver;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class LoanService {

    private final LoanRepository loanRepo;
    private final UserRepository userRepo;
    private final BookRepository bookRepo;
    private final List<LoanObserver> observers = new CopyOnWriteArrayList<>();

    public LoanService(LoanRepository loanRepo, UserRepository userRepo, BookRepository bookRepo, NotificationService notificationService) {
        this.loanRepo = loanRepo;
        this.userRepo = userRepo;
        this.bookRepo = bookRepo;
        this.addObserver(notificationService);
    }

    public void addObserver(LoanObserver o) {
        observers.add(o);
    }

    public List<Loan> findAll() {
        return loanRepo.findAll();
    }

    public List<Loan> findByUserId(Long userId) {
        return loanRepo.findByUserId(userId);
    }

    public Loan create(LoanDTO dto) {
        User user = userRepo.findById(dto.userId())
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));
        Book book = bookRepo.findById(dto.bookId())
                .orElseThrow(() -> new RuntimeException("Livre introuvable"));
        if (book.getStatus() == BookStatus.BORROWED)
            throw new RuntimeException("Livre déjà emprunté");

        Loan loan = LoanFactory.createStandardLoan(user, book);
        Loan saved = loanRepo.save(loan);

        book.setStatus(BookStatus.BORROWED);
        bookRepo.save(book);

        notifyCreated(saved);
        return saved;
    }

    public Loan returnBook(Long id) {
        Loan loan = loanRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Emprunt introuvable"));

        Book book = loan.getBook();
        book.setStatus(BookStatus.AVAILABLE);
        bookRepo.save(book);

        loan.setStatus(LoanStatus.RETURNED);
        loan.setReturnDate(LocalDateTime.now());
        return loanRepo.save(loan);
    }

    private void notifyCreated(Loan loan) {
        observers.forEach(o -> o.onCreated(loan));
    }
}

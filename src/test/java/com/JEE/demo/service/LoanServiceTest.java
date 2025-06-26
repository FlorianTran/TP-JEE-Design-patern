package com.JEE.demo.service;

import com.JEE.demo.dto.LoanDTO;
import com.JEE.demo.entity.Book;
import com.JEE.demo.entity.Loan;
import com.JEE.demo.entity.User;
import com.JEE.demo.entity.enums.BookStatus;
import com.JEE.demo.entity.enums.LoanStatus;
import com.JEE.demo.repository.BookRepository;
import com.JEE.demo.repository.LoanRepository;
import com.JEE.demo.repository.UserRepository;
import com.JEE.demo.service.observer.LoanObserver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class LoanServiceTest {

    @Mock private LoanRepository loanRepo;
    @Mock private UserRepository userRepo;
    @Mock private BookRepository bookRepo;
    @Mock private NotificationService notification;

    private LoanService service;

    private User user;
    private Book book;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
        user = new User("anna@example.com", "Anna", "Smith");
        user.setStatus(com.JEE.demo.entity.enums.UserStatus.ACTIVE);
        book = new Book("Domain-Driven Design", "Eric Evans", "3333333333");

        // création manuelle du service avec tous les arguments
        service = new LoanService(loanRepo, userRepo, bookRepo, notification);
    }

    @Test
    void create_throwsWhenUserNotFound() {
        when(userRepo.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.create(new LoanDTO(null, 1L, 1L)))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Utilisateur introuvable");
    }

    @Test
    void create_throwsWhenBookNotFound() {
        when(userRepo.findById(1L)).thenReturn(Optional.of(user));
        when(bookRepo.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.create(new LoanDTO(null, 1L, 1L)))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Livre introuvable");
    }

    @Test
    void create_throwsWhenBookAlreadyBorrowed() {
        book.setStatus(BookStatus.BORROWED);
        when(userRepo.findById(1L)).thenReturn(Optional.of(user));
        when(bookRepo.findById(1L)).thenReturn(Optional.of(book));

        assertThatThrownBy(() -> service.create(new LoanDTO(null, 1L, 1L)))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Livre déjà emprunté");
    }

    @Test
    void create_succeedsAndNotifiesObservers() {
        when(userRepo.findById(1L)).thenReturn(Optional.of(user));
        when(bookRepo.findById(1L)).thenReturn(Optional.of(book));
        when(loanRepo.save(any(Loan.class))).thenAnswer(inv -> inv.getArgument(0));

        Loan loan = service.create(new LoanDTO(null, 1L, 1L));

        assertThat(loan.getStatus()).isEqualTo(LoanStatus.ACTIVE);
        assertThat(book.getStatus()).isEqualTo(BookStatus.BORROWED);

        verify(notification).onCreated(loan);
        verify(bookRepo).save(book);
    }

    @Test
    void returnBook_setsBookAvailableAndLoanReturned() {
        Loan loan = new Loan(user, book);
        when(loanRepo.findById(1L)).thenReturn(Optional.of(loan));
        when(loanRepo.save(any())).thenAnswer(inv -> inv.getArgument(0));
        when(bookRepo.save(any())).thenAnswer(inv -> inv.getArgument(0));

        Loan result = service.returnBook(1L);

        assertThat(book.getStatus()).isEqualTo(BookStatus.AVAILABLE);
        assertThat(result.getStatus()).isEqualTo(LoanStatus.RETURNED);
        assertThat(result.getReturnDate()).isNotNull();
        verify(bookRepo).save(book);
    }

    @Test
    void findAll_returnsAllLoans() {
        Loan loan = new Loan(user, book);
        when(loanRepo.findAll()).thenReturn(List.of(loan));

        assertThat(service.findAll()).containsExactly(loan);
    }

    @Test
    void findByUserId_returnsMatchingLoans() {
        Loan loan = new Loan(user, book);
        when(loanRepo.findByUserId(1L)).thenReturn(List.of(loan));

        assertThat(service.findByUserId(1L)).containsExactly(loan);
    }

    @Test
    void onCreated_logsNotification() {
        NotificationService notification = new NotificationService();
        Loan loan = mock(Loan.class);
        User mockUser = new User("user@test.com", "Jane", "Doe");
        Book mockBook = new Book("Design Patterns", "GoF", "9781234567890");
        when(loan.getUser()).thenReturn(mockUser);
        when(loan.getBook()).thenReturn(mockBook);

        notification.onCreated(loan);
    }
}

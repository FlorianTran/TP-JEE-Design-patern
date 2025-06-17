package com.JEE.demo.service;

import com.JEE.demo.dto.LoanDTO;
import com.JEE.demo.entity.*;
import com.JEE.demo.entity.enums.BookStatus;
import com.JEE.demo.entity.enums.LoanStatus;
import com.JEE.demo.repository.*;
import com.JEE.demo.service.observer.LoanObserver;
import org.junit.jupiter.api.*;
import org.mockito.*;
import java.util.Optional;
import static org.assertj.core.api.Assertions.*;

class LoanServiceTest {

    @Mock private LoanRepository loanRepo;
    @Mock private UserRepository userRepo;
    @Mock private BookRepository bookRepo;
    @InjectMocks private LoanService service;

    private User user;
    private Book book;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
        user = new User("anna@example.com", "Anna", "Smith");
        user.setStatus(com.JEE.demo.entity.enums.UserStatus.ACTIVE);
        book = new Book("Domain-Driven Design", "Eric Evans", "3333333333");
    }

    @Test
    void create_throwsWhenUserNotFound() {
        Mockito.when(userRepo.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.create(new LoanDTO(null, 1L, 1L)))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Utilisateur introuvable");
    }

    @Test
    void create_throwsWhenBookNotFound() {
        Mockito.when(userRepo.findById(1L)).thenReturn(Optional.of(user));
        Mockito.when(bookRepo.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.create(new LoanDTO(null, 1L, 1L)))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Livre introuvable");
    }

    @Test
    void create_throwsWhenBookAlreadyBorrowed() {
        book.setStatus(BookStatus.BORROWED);
        Mockito.when(userRepo.findById(1L)).thenReturn(Optional.of(user));
        Mockito.when(bookRepo.findById(1L)).thenReturn(Optional.of(book));

        assertThatThrownBy(() -> service.create(new LoanDTO(null, 1L, 1L)))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Livre déjà emprunté");
    }

    @Test
    void create_succeedsAndNotifiesObservers() {
        Mockito.when(userRepo.findById(1L)).thenReturn(Optional.of(user));
        Mockito.when(bookRepo.findById(1L)).thenReturn(Optional.of(book));
        Mockito.when(loanRepo.save(Mockito.any(Loan.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        // Fake observer
        LoanObserver obs = Mockito.mock(LoanObserver.class);
        service.addObserver(obs);

        Loan loan = service.create(new LoanDTO(null, 1L, 1L));

        assertThat(loan.getStatus()).isEqualTo(LoanStatus.ACTIVE);
        assertThat(book.getStatus()).isEqualTo(BookStatus.BORROWED);

        Mockito.verify(obs).onCreated(loan);
        Mockito.verify(bookRepo).save(book);
    }
}

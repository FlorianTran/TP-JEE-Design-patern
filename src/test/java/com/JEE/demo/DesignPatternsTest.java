package com.JEE.demo;

import com.JEE.demo.dto.BookDTO;
import com.JEE.demo.dto.LoanDTO;
import com.JEE.demo.dto.UserDTO;
import com.JEE.demo.entity.*;
import com.JEE.demo.entity.enums.BookStatus;
import com.JEE.demo.factory.LoanFactory;
import com.JEE.demo.service.NotificationService;
import com.JEE.demo.strategy.EmailValidationStrategy;
import com.JEE.demo.strategy.IsbnValidationStrategy;
import com.JEE.demo.service.observer.LoanObserver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.*;

public class DesignPatternsTest {

    private User sampleUser;
    private Book sampleBook;

    @BeforeEach
    public void setUp() {
        sampleUser = new User("john.doe@example.com", "John", "Doe");
        sampleBook = new Book("Java", "Martin", "1234567890");
    }

    @Test
    public void singleton_shouldReturnSameInstance() {
        NotificationService instance1 = NotificationService.getInstance();
        NotificationService instance2 = NotificationService.getInstance();
        assertSame(instance1, instance2);
    }

    @Test
    public void factory_shouldCreateLoan() {
        Loan loan = LoanFactory.createStandardLoan(sampleUser, sampleBook);
        assertNotNull(loan);
        assertEquals(sampleUser, loan.getUser());
        assertEquals(sampleBook, loan.getBook());
        assertEquals(BookStatus.AVAILABLE, sampleBook.getStatus());
    }

    @Test
    public void strategy_shouldValidateEmailAndIsbn() {
        UserDTO validUser = new UserDTO(1L,"valid@mail.com", "First", "Last");
        BookDTO validBook = new BookDTO(2L, "Book", "Author", "9783161484100");

        EmailValidationStrategy emailValidator = new EmailValidationStrategy();
        IsbnValidationStrategy isbnValidator = new IsbnValidationStrategy();

        assertDoesNotThrow(() -> emailValidator.validate(validUser));
        assertDoesNotThrow(() -> isbnValidator.validate(validBook));
    }

    @Test
    public void observer_shouldTriggerOnCreated() {
        AtomicBoolean triggered = new AtomicBoolean(false);

        LoanObserver observer = loan -> triggered.set(true);
        Loan loan = new Loan(sampleUser, sampleBook);
        observer.onCreated(loan);

        assertTrue(triggered.get());
    }
}

package com.JEE.demo.strategy;

import com.JEE.demo.dto.BookDTO;
import com.JEE.demo.dto.UserDTO;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ValidationStrategyTest {

    private final EmailValidationStrategy emailVal = new EmailValidationStrategy();
    private final IsbnValidationStrategy  isbnVal  = new IsbnValidationStrategy();

    @Test
    void emailValidation_acceptsValidAddress() {
        assertDoesNotThrow(() ->
                emailVal.validate(new UserDTO(null, "alice@example.com", "Alice", "Liddell")));
    }

    @Test
    void emailValidation_refusesInvalidAddress() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> emailVal.validate(new UserDTO(null, "alice-example.com", "Alice", "Liddell")));
        assertEquals("Adresse e-mail invalide", ex.getMessage());
    }

    @Test
    void isbnValidation_accepts10OrMoreChars() {
        assertDoesNotThrow(() ->
                isbnVal.validate(new BookDTO(null, "Titre", "Auteur", "0123456789")));
    }

    @Test
    void isbnValidation_refusesTooShortIsbn() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> isbnVal.validate(new BookDTO(null, "Titre", "Auteur", "123456")));
        assertEquals("ISBN invalide", ex.getMessage());
    }
}

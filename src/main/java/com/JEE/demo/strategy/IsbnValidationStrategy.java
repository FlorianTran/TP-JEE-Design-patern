package com.JEE.demo.strategy;

import com.JEE.demo.dto.BookDTO;
import org.springframework.stereotype.Component;

@Component
public class IsbnValidationStrategy implements ValidationStrategy<BookDTO> {
    @Override
    public void validate(BookDTO dto) {
        if (dto.isbn().length() < 10)
            throw new IllegalArgumentException("ISBN invalide");
    }
}

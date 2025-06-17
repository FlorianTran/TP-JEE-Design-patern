package com.JEE.demo.strategy;

import com.JEE.demo.dto.UserDTO;
import org.springframework.stereotype.Component;

@Component
public class EmailValidationStrategy implements ValidationStrategy<UserDTO> {
    @Override
    public void validate(UserDTO dto) {
        if (!dto.email().contains("@"))
            throw new IllegalArgumentException("Adresse e-mail invalide");
    }
}

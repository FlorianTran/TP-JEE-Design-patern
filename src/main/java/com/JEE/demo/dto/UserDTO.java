package com.JEE.demo.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserDTO(
        Long id,
        @Email @NotBlank String email,
        @NotBlank String firstName,
        @NotBlank String lastName
) {}

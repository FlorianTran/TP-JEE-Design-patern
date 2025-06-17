package com.JEE.demo.dto;

import jakarta.validation.constraints.NotBlank;

public record BookDTO(
        Long id,
        @NotBlank String title,
        @NotBlank String author,
        @NotBlank String isbn
) {}

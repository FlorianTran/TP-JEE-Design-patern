package com.JEE.demo.dto;

import jakarta.validation.constraints.NotNull;

public record LoanDTO(
        Long id,
        @NotNull Long userId,
        @NotNull Long bookId
) {}

package com.JEE.demo.controller;

import com.JEE.demo.dto.LoanDTO;
import com.JEE.demo.entity.Loan;
import com.JEE.demo.service.LoanService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/loans")
public class LoanController {

    private final LoanService service;

    public LoanController(LoanService service) { this.service = service; }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Loan create(@Valid @RequestBody LoanDTO dto) { return service.create(dto); }
}

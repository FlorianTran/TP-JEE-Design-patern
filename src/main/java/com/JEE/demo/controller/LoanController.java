package com.JEE.demo.controller;

import com.JEE.demo.dto.LoanDTO;
import com.JEE.demo.entity.Loan;
import com.JEE.demo.service.LoanService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/loans")
public class LoanController {

    private final LoanService service;

    public LoanController(LoanService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Loan> create(@Valid @RequestBody LoanDTO dto) {
        return ResponseEntity.status(201).body(service.create(dto));
    }

    @GetMapping
    public ResponseEntity<List<Loan>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @PutMapping("/{id}/return")
    public ResponseEntity<Loan> returnBook(@PathVariable Long id) {
        return ResponseEntity.ok(service.returnBook(id));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Loan>> findByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(service.findByUserId(userId));
    }
}

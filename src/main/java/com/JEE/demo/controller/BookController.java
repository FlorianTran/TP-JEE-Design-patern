package com.JEE.demo.controller;

import com.JEE.demo.dto.BookDTO;
import com.JEE.demo.entity.Book;
import com.JEE.demo.entity.enums.BookStatus;
import com.JEE.demo.service.BookService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookController {

    private final BookService service;

    public BookController(BookService service) {
        this.service = service;
    }

    @PostMapping
    public Book create(@Valid @RequestBody BookDTO dto) {
        return service.create(dto);
    }

    @GetMapping
    public ResponseEntity<List<Book>> getBooks(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String author,
            @RequestParam(required = false) BookStatus status
    ) {
        return ResponseEntity.ok(service.findByFilters(title, author, status));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Book> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Book> update(@PathVariable Long id, @Valid @RequestBody BookDTO dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}

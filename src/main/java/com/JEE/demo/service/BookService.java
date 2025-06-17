package com.JEE.demo.service;

import com.JEE.demo.dto.BookDTO;
import com.JEE.demo.entity.Book;
import com.JEE.demo.entity.enums.BookStatus;
import com.JEE.demo.repository.BookRepository;
import com.JEE.demo.strategy.IsbnValidationStrategy;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class BookService {

    private final BookRepository repo;
    private final IsbnValidationStrategy validator;

    public BookService(BookRepository repo, IsbnValidationStrategy validator) {
        this.repo = repo;
        this.validator = validator;
    }

    public List<Book> findAll() { return repo.findAll(); }

    public Book create(BookDTO dto) {
        validator.validate(dto);
        Book book = new Book(dto.title(), dto.author(), dto.isbn());
        return repo.save(book);
    }

    public List<Book> findByFilters(String title, String author, BookStatus status) {
        return repo.findAll().stream()
                .filter(book -> title == null || book.getTitle().toLowerCase().contains(title.toLowerCase()))
                .filter(book -> author == null || book.getAuthor().toLowerCase().contains(author.toLowerCase()))
                .filter(book -> status == null || book.getStatus() == status)
                .toList();
    }

}

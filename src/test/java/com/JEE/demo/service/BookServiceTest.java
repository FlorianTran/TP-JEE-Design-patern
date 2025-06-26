package com.JEE.demo.service;

import com.JEE.demo.dto.BookDTO;
import com.JEE.demo.entity.Book;
import com.JEE.demo.entity.enums.BookStatus;
import com.JEE.demo.repository.BookRepository;
import com.JEE.demo.strategy.IsbnValidationStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

class BookServiceTest {

    @Mock private BookRepository repo;
    @Mock private IsbnValidationStrategy validator;
    @InjectMocks private BookService service;

    @BeforeEach
    void init() { MockitoAnnotations.openMocks(this); }

    @Test
    void create_callsValidatorAndPersistsBook() {
        BookDTO dto = new BookDTO(null, "1984", "Orwell", "0123456789");
        Book saved  = new Book(dto.title(), dto.author(), dto.isbn());

        Mockito.when(repo.save(Mockito.any(Book.class))).thenReturn(saved);

        Book result = service.create(dto);

        Mockito.verify(validator).validate(dto);
        Mockito.verify(repo).save(Mockito.any(Book.class));
        assertThat(result)
                .extracting(Book::getTitle, Book::getAuthor, Book::getStatus)
                .containsExactly("1984", "Orwell", BookStatus.AVAILABLE);
    }

    @Test
    void findByFilters_filtersByTitleAuthorAndStatus() {
        Book b1 = new Book("Spring Boot", "Rod Johnson", "1111111111");
        Book b2 = new Book("Clean Code", "Robert C. Martin", "2222222222");
        b2.setStatus(BookStatus.BORROWED);
        Mockito.when(repo.findAll()).thenReturn(List.of(b1, b2));

        List<Book> onlyCleanCode = service.findByFilters("clean", null, null);
        assertThat(onlyCleanCode).containsExactly(b2);

        List<Book> borrowedBooks = service.findByFilters(null, null, BookStatus.BORROWED);
        assertThat(borrowedBooks).containsExactly(b2);

        List<Book> nothing = service.findByFilters("java", "martin", BookStatus.AVAILABLE);
        assertThat(nothing).isEmpty();
    }

    @Test
    void findById_returnsCorrectBook() {
        Book book = new Book("Effective Java", "Joshua Bloch", "9999999999");
        Mockito.when(repo.findById(1L)).thenReturn(Optional.of(book));

        Book result = service.findById(1L);
        assertThat(result).isEqualTo(book);
    }

    @Test
    void update_updatesBookFieldsCorrectly() {
        Book original = new Book("Old Title", "Old Author", "111");
        BookDTO update = new BookDTO(null, "New Title", "New Author", "111");

        Mockito.when(repo.findById(1L)).thenReturn(Optional.of(original));
        Mockito.when(repo.save(Mockito.any())).thenAnswer(inv -> inv.getArgument(0));

        Book result = service.update(1L, update);

        assertThat(result.getTitle()).isEqualTo("New Title");
        assertThat(result.getAuthor()).isEqualTo("New Author");
    }

    @Test
    void delete_removesBook() {
        Book book = new Book("To Delete", "Someone", "000");
        Mockito.when(repo.findById(1L)).thenReturn(Optional.of(book));

        service.delete(1L);

        Mockito.verify(repo).deleteById(1L);
    }
}

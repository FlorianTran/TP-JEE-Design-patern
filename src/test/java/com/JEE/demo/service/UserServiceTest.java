package com.JEE.demo.service;

import com.JEE.demo.dto.UserDTO;
import com.JEE.demo.entity.User;
import com.JEE.demo.repository.UserRepository;
import com.JEE.demo.strategy.EmailValidationStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

class UserServiceTest {

    @Mock private UserRepository repo;
    @Mock private EmailValidationStrategy validator;
    @InjectMocks private UserService service;

    @BeforeEach
    void init() { MockitoAnnotations.openMocks(this); }

    @Test
    void create_persistsUserAndValidatesEmail() {
        UserDTO dto = new UserDTO(null, "bob@example.com", "Bob", "Builder");
        User saved  = new User(dto.email(), dto.firstName(), dto.lastName());

        Mockito.when(repo.save(Mockito.any(User.class))).thenReturn(saved);

        User result = service.create(dto);

        Mockito.verify(validator).validate(dto);
        Mockito.verify(repo).save(Mockito.any(User.class));
        assertThat(result.getEmail()).isEqualTo("bob@example.com");
    }

    @Test
    void findById_returnsCorrectUser() {
        User u = new User("x@y.z", "X", "Z");
        Mockito.when(repo.findById(1L)).thenReturn(Optional.of(u));

        assertThat(service.findById(1L)).isEqualTo(u);
    }

    @Test
    void update_modifiesUserProperly() {
        User old = new User("a@a.com", "A", "B");
        UserDTO dto = new UserDTO(null, "b@b.com", "C", "D");

        Mockito.when(repo.findById(1L)).thenReturn(Optional.of(old));
        Mockito.when(repo.save(Mockito.any())).thenAnswer(inv -> inv.getArgument(0));

        User result = service.update(1L, dto);

        assertThat(result.getEmail()).isEqualTo("b@b.com");
        assertThat(result.getFirstName()).isEqualTo("C");
        assertThat(result.getLastName()).isEqualTo("D");
    }

    @Test
    void delete_removesUser() {
        User u = new User("test@test.com", "Test", "User");
        Mockito.when(repo.findById(1L)).thenReturn(Optional.of(u));

        service.delete(1L);

        Mockito.verify(repo).deleteById(1L);
    }
}

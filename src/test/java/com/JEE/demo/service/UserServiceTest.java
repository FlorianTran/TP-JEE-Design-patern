package com.JEE.demo.service;

import com.JEE.demo.dto.UserDTO;
import com.JEE.demo.entity.User;
import com.JEE.demo.repository.UserRepository;
import com.JEE.demo.strategy.EmailValidationStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
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
}

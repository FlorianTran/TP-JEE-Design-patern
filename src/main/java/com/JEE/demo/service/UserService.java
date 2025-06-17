package com.JEE.demo.service;

import com.JEE.demo.dto.UserDTO;
import com.JEE.demo.entity.User;
import com.JEE.demo.repository.UserRepository;
import com.JEE.demo.strategy.EmailValidationStrategy;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UserService {

    private final UserRepository repo;
    private final EmailValidationStrategy validator;

    public UserService(UserRepository repo, EmailValidationStrategy validator) {
        this.repo = repo;
        this.validator = validator;
    }

    public List<User> findAll() { return repo.findAll(); }

    public User create(UserDTO dto) {
        validator.validate(dto);
        User user = new User(dto.email(), dto.firstName(), dto.lastName());
        return repo.save(user);
    }
}

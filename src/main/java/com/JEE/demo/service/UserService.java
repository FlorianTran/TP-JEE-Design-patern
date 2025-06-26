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

    public List<User> findAll() {
        return repo.findAll();
    }

    public User findById(Long id) {
        return repo.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
    }

    public User create(UserDTO dto) {
        validator.validate(dto);
        User user = new User(dto.email(), dto.firstName(), dto.lastName());
        return repo.save(user);
    }

    public User update(Long id, UserDTO dto) {
        User existing = findById(id);
        validator.validate(dto);
        existing.setEmail(dto.email());
        existing.setFirstName(dto.firstName());
        existing.setLastName(dto.lastName());
        return repo.save(existing);
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }
}

package com.brilworks.mockup.modules.user.service;

import com.brilworks.mockup.modules.user.model.AuthUser;
import com.brilworks.mockup.modules.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public AuthUser createUser(AuthUser user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public Optional<AuthUser> getUserById(Integer id) {
        return userRepository.findById(id);
    }

    public AuthUser updateUser(AuthUser user) {
        return userRepository.save(user);
    }

    public void deleteUser(Integer id) {
        userRepository.deleteById(id);
    }

    public Page<AuthUser> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    public Page<AuthUser> searchUsers(String searchQuery, Pageable pageable) {
        return userRepository.findByNameContainingIgnoreCaseOrEmailContainingIgnoreCase(searchQuery, searchQuery, pageable);
    }
}


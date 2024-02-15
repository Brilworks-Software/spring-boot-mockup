package com.brilworks.mockup.modules.auth.service;

import com.brilworks.mockup.modules.auth.dto.LoginRequest;
import com.brilworks.mockup.modules.user.model.AuthUser;
import com.brilworks.mockup.modules.user.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.security.sasl.AuthenticationException;

@Service
public class AuthUserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthUserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public AuthUser authenticate(LoginRequest loginRequest) throws AuthenticationException {
        AuthUser user = userRepository.findByEmail(loginRequest.getUserName()).orElseThrow(() -> new AuthenticationException("Invalid username or password"));
        boolean matches = passwordEncoder.matches(loginRequest.getPassword(), user.getPassword());
        if (!matches) {
            throw new AuthenticationException("Invalid username or password");
        }
        return user;
    }
}

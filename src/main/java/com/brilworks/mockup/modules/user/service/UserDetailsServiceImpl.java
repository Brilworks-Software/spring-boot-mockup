package com.brilworks.mockup.modules.user.service;

import com.brilworks.mockup.modules.auth.dto.AuthenticatedUser;
import com.brilworks.mockup.modules.user.model.AuthUser;
import com.brilworks.mockup.modules.user.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public AuthenticatedUser loadUserByUsername(String userName) throws UsernameNotFoundException {
        AuthUser user = userRepository.findByEmail(userName)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found"));
        return new AuthenticatedUser(user.getEmail(), user.getPassword(), Collections.EMPTY_LIST);
    }
}

package com.brilworks.mockup.service.user;

import com.brilworks.mockup.dto.auth.AuthenticatedUser;
import com.brilworks.mockup.entity.User;
import com.brilworks.mockup.repository.UserRepository;
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
        User user = userRepository.findByEmail(userName)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found"));
        return new AuthenticatedUser(user.getEmail(), user.getPassword(), Collections.EMPTY_LIST);
    }
}

package com.brilworks.mockup.service;


import com.brilworks.mockup.modules.user.repository.UserRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
    }

    @Test
    void testCreateUser() {
        User user = new User();
        when(userRepository.save(any())).thenReturn(user);

        User createdUser = userService.createUser(user);

        assertEquals(user, createdUser);
    }

    @Test
    void testGetUserById() {
        int userId = 1;
        User user = new User(userId);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        Optional<User> retrievedUser = userService.getUserById(userId);

        assertEquals(Optional.of(user), retrievedUser);
    }

    @Test
    void testUpdateUser() {
        User user = new User();
        when(userRepository.save(any())).thenReturn(user);

        User updatedUser = userService.updateUser(user);

        assertEquals(user, updatedUser);
    }

    @Test
    void testDeleteUser() {
        int userId = 1;
        doNothing().when(userRepository).deleteById(userId);

        userService.deleteUser(userId);

        verify(userRepository, times(1)).deleteById(userId);
    }

    @Test
    void testGetAllUsers() {
        List<User> userList = new ArrayList<>();
        userList.add(new User(1));
        userList.add(new User(2));
        Page<User> page = new PageImpl<>(userList);
        when(userRepository.findAll(any(Pageable.class))).thenReturn(page);

        Page<User> allUsersPage = userService.getAllUsers(Pageable.unpaged());

        assertEquals(page, allUsersPage);
    }

    @Test
    void testSearchUsers() {
        String searchQuery = "John";
        List<User> userList = new ArrayList<>();
        userList.add(new User(1, "John Doe", "john.doe@example.com"));
        userList.add(new User(2, "Jane Smith", "jane.smith@example.com"));
        Page<User> page = new PageImpl<>(userList);
        when(userRepository.findByNameContainingIgnoreCaseOrEmailContainingIgnoreCase(anyString(), anyString(), any(Pageable.class)))
                .thenReturn(page);

        Page<User> searchedUsersPage = userService.searchUsers(searchQuery, Pageable.unpaged());

        assertEquals(page, searchedUsersPage);
    }
}


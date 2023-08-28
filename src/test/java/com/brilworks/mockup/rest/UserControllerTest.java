package com.brilworks.mockup.rest;

import com.brilworks.mockup.dto.ResponseListEntity;
import com.brilworks.mockup.entity.User;
import com.brilworks.mockup.rest.user.UserController;
import com.brilworks.mockup.service.user.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUp() {
    }

    @Test
    void testCreateUser() {
        User user = new User();
        when(userService.createUser(any())).thenReturn(user);

        ResponseEntity<User> responseEntity = userController.createUser(user);

        assertEquals(user, responseEntity.getBody());
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
    }

    @Test
    void testGetUserById() {
        int userId = 1;
        User user = new User(userId);
        when(userService.getUserById(userId)).thenReturn(Optional.of(user));

        ResponseEntity<User> responseEntity = userController.getUserById(userId);

        assertEquals(user, responseEntity.getBody());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    void testUpdateUser() {
        int userId = 1;
        User user = new User(userId);
        when(userService.updateUser(any())).thenReturn(user);

        ResponseEntity<User> responseEntity = userController.updateUser(userId, user);

        assertEquals(user, responseEntity.getBody());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    void testDeleteUser() {
        int userId = 1;
        doNothing().when(userService).deleteUser(userId);

        ResponseEntity<Void> responseEntity = userController.deleteUser(userId);

        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
    }

    @Test
    void testGetAllUsersWithoutSearchQuery() {
        int page = 0;
        int size = 2;
        Pageable pageable = PageRequest.of(page, size);

        List<User> userList = new ArrayList<>();
        userList.add(new User(1, "John Doe", "john.doe@example.com"));
        userList.add(new User(2, "Jane Smith", "jane.smith@example.com"));
        Page<User> pageResult = new PageImpl<>(userList);
        when(userService.getAllUsers(pageable)).thenReturn(pageResult);

        ResponseEntity<ResponseListEntity<User>> response = userController.getAllUsers(page, size, null);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(userList, response.getBody().getData());
        assertEquals(userList.size(), response.getBody().getCount());
    }

    @Test
    void testGetAllUsersWithSearchQuery() {
        int page = 0;
        int size = 2;
        Pageable pageable = PageRequest.of(page, size);

        String searchQuery = "John";
        List<User> userList = new ArrayList<>();
        userList.add(new User(1, "John Doe", "john.doe@example.com"));
        Page<User> pageResult = new PageImpl<>(userList);
        when(userService.searchUsers(searchQuery, pageable)).thenReturn(pageResult);

        ResponseEntity<ResponseListEntity<User>> response = userController.getAllUsers(page, size, searchQuery);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(userList, response.getBody().getData());
        assertEquals(userList.size(), response.getBody().getCount());
    }
}

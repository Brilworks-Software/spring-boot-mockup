package com.brilworks.mockup.modules.user.rest;

import com.brilworks.mockup.dto.ResponseListEntity;
import com.brilworks.mockup.modules.user.model.AuthUser;
import com.brilworks.mockup.exceptions.EntityNotFoundException;
import com.brilworks.mockup.modules.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    @Operation(summary = "Create a new user")
    public ResponseEntity<AuthUser> createUser(@RequestBody AuthUser user) {
        AuthUser createdUser = userService.createUser(user);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a user by ID")
    public ResponseEntity<AuthUser> getUserById(@PathVariable Integer id) {
        AuthUser user = userService.getUserById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + id));
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing user")
    public ResponseEntity<AuthUser> updateUser(@PathVariable Integer id, @RequestBody AuthUser user) {
        user.setId(id);
        AuthUser updatedUser = userService.updateUser(user);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a user by ID")
    public ResponseEntity<Void> deleteUser(@PathVariable Integer id) {
        userService.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get all users with pagination and search options")
    public ResponseEntity<ResponseListEntity<AuthUser>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String searchQuery
    ) {
        // Create a Pageable object to handle pagination
        Pageable pageable = PageRequest.of(page, size);

        Page<AuthUser> users;
        if (searchQuery != null && !searchQuery.isEmpty()) {
            // If a search query is provided, search for users by name or email
            users = userService.searchUsers(searchQuery, pageable);
        } else {
            // If no search query is provided, fetch all users with pagination
            users = userService.getAllUsers(pageable);
        }
        ResponseListEntity<AuthUser> responseListEntity = new ResponseListEntity<>(users.getContent(), users.getTotalElements());
        return new ResponseEntity<>(responseListEntity, HttpStatus.OK);
    }
}

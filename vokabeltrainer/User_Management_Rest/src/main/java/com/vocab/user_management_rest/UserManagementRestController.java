package com.vocab.user_management_rest;

import com.vocab.user_management.entities.UserEntity;
import com.vocab.user_management.exceptions.UserNotExistException;
import com.vocab.user_management.exceptions.UserStillPlaysException;
import com.vocab.user_management.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@ComponentScan(basePackages = {"com.vocab"})
@CrossOrigin(origins = "http://localhost:5173") // Frontend-Server-URL
public class UserManagementRestController {
    @Autowired
    private UserService userService;

    @GetMapping("/api/user")
    public ResponseEntity<List<UserEntity>> getAllUsers() {
        return ResponseEntity.ok(userService.getAll());
    }

    @GetMapping("/api/user/byid")
    public ResponseEntity<UserEntity> getUserById(@RequestParam Long userId) throws UserNotExistException {
        UserEntity user = userService.getById(userId);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/api/user/byusername")
    public ResponseEntity<UserEntity> getUserByUsername(@RequestParam String username) throws UserNotExistException {
        UserEntity user = userService.findByUsername(username);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/api/user")
    public ResponseEntity<UserEntity> createUser(@RequestBody UserEntity request) {
        UserEntity user = userService.createUserRest(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    @DeleteMapping("/api/user")
    public ResponseEntity<Void> deleteUser(@RequestParam Long userId) throws UserNotExistException, UserStillPlaysException {
        userService.deleteUser(userId);
        return ResponseEntity.ok().build();
    }
}

package com.vocab.user_management_rest;

import com.vocab.user_management.entities.UserEntity;
import com.vocab.user_management.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@ComponentScan(basePackages = {"com.vocab"})
@CrossOrigin(origins = "http://localhost:5173") // Frontend-Server-URL
public class UserManagamentRestController {
    @Autowired
    private UserService userService;

    @GetMapping("/api/user")
    public ResponseEntity<List<UserEntity>> getAllUsers() {
        try {
            return ResponseEntity.ok(userService.getAll());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Allgemeiner technischer Fehler.");
        }
    }

    @GetMapping("/api/user/byid")
    public ResponseEntity<UserEntity> getUserById(@RequestParam Long userid) {
        UserEntity user = userService.getById(userid);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/api/user/byusername")
    public ResponseEntity<UserEntity> getUserByUsername(@RequestParam String username) {
        UserEntity user = userService.findByUsername(username);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/api/user")
    public ResponseEntity<UserEntity> createUser(@RequestBody UserEntity request) {
        UserEntity user = userService.createUserRest(request);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Allgemeiner technischer Fehler.");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    @DeleteMapping("/api/user")
    public ResponseEntity<Void> deleteUser(@RequestParam Long userid) {
        if (userService.deleteUser(userid)) {
            return ResponseEntity.ok().build();
        } else return ResponseEntity.notFound().build();
    }
}

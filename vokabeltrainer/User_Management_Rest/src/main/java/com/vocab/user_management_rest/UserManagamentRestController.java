package com.vocab.user_management_rest;

import com.vocab.user_management.entities.UserEntity;
import com.vocab.user_management.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
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
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/api/user/byid")
    public ResponseEntity<UserEntity> getUserById(@RequestParam Long userid) {
        try {
            UserEntity user = userService.getById(userid);
            return ResponseEntity.ok(user);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/api/user/byusername")
    public ResponseEntity<UserEntity> getUserByUsername(@RequestParam String username) {
        try {
            UserEntity user = userService.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
            return ResponseEntity.ok(user);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/api/user")
    public ResponseEntity<UserEntity> createUser(@RequestBody UserEntity request) throws URISyntaxException {
        try {
            UserEntity user = userService.createUserRest(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(user);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping("/api/user")
    public ResponseEntity<Void> deleteUser(@RequestParam Long userid) {
        try {
            if(userService.deleteUser(userid)) {
                return ResponseEntity.ok().build();
            }else return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}

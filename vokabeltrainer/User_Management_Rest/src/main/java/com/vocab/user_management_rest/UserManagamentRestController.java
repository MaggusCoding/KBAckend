package com.vocab.user_management_rest;

import com.vocab.user_management.entities.UserEntity;
import com.vocab.user_management.services.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
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
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/api/user/byid")
    public ResponseEntity<UserEntity> getUserById(@RequestParam Long userid) {
        try {
            UserEntity user = userService.getById(userid);
            return ResponseEntity.ok(user);
        } catch(EntityNotFoundException enfe){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, enfe.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/api/user/byusername")
    public ResponseEntity<UserEntity> getUserByUsername(@RequestParam String username) {
        try {
            UserEntity user = userService.findByUsername(username);
            return ResponseEntity.ok(user);
        } catch(EntityNotFoundException enfe){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, enfe.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/api/user")
    public ResponseEntity<UserEntity> createUser(@RequestBody UserEntity request) {
        try {
            UserEntity user = userService.createUserRest(request);
            if(user == null){
                return ResponseEntity.internalServerError().build();
            }
            return ResponseEntity.status(HttpStatus.CREATED).body(user);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping("/api/user")
    public ResponseEntity<Void> deleteUser(@RequestParam Long userid) {
        try {
            if (userService.deleteUser(userid)) {
                return ResponseEntity.ok().build();
            } else return ResponseEntity.notFound().build();
        } catch (ObjectOptimisticLockingFailureException oe) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "User-Daten nicht aktuell. Bitte neu laden!");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}

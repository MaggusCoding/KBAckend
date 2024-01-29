package com.vocab.user_management_rest;

import com.vocab.user_management.dtos.UserDTO;
import com.vocab.user_management.entities.UserEntity;
import com.vocab.user_management.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@ComponentScan(basePackages = {"com.vocab"})
@CrossOrigin(origins = "http://localhost:5173") // Frontend-Server-URL
public class UserManagementRestController {
    @Autowired
    private UserService userService;

    @GetMapping("/api/user")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        try {
            return ResponseEntity.ok(userService.getAll().stream().map(UserDTO::fromEntity).collect(Collectors.toList()));
        } catch (Exception e) {
            UserDTO error = new UserDTO();
            error.setErrorMessage(e.getMessage());
            return ResponseEntity.internalServerError().body(List.of(error));
        }
    }

    @GetMapping("/api/user/byid")
    public ResponseEntity<UserDTO> getUserById(@RequestParam Long userId) {
        try {
            UserEntity user = userService.getById(userId);
            return ResponseEntity.ok(UserDTO.fromEntity(user));
        } catch (RuntimeException e) {
            UserDTO error = new UserDTO();
            error.setErrorMessage(e.getMessage());
            return ResponseEntity.internalServerError().body(error);
        }
    }

    @GetMapping("/api/user/byusername")
    public ResponseEntity<UserDTO> getUserByUsername(@RequestParam String username) {
        try {
            UserEntity user = userService.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
            return ResponseEntity.ok(UserDTO.fromEntity(user));
        } catch (RuntimeException e) {
            UserDTO error = new UserDTO();
            error.setErrorMessage(e.getMessage());
            return ResponseEntity.internalServerError().body(error);
        }
    }

    @PostMapping("/api/user")
    public ResponseEntity<UserDTO> createUser(@RequestBody UserEntity request) {
        try {
            UserEntity user = userService.createUserRest(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(UserDTO.fromEntity(user));
        } catch (Exception e) {
            UserDTO error = new UserDTO();
            error.setErrorMessage(e.getMessage());
            return ResponseEntity.internalServerError().body(error);
        }
    }

    @DeleteMapping("/api/user")
    public ResponseEntity<UserDTO> deleteUser(@RequestParam Long userId) {
        try {
            if (userService.deleteUser(userId)) {
                return ResponseEntity.ok().build();
            } else {
                UserDTO error = new UserDTO();
                error.setErrorMessage("User mit id " + userId + " nicht gefunden.");
                return ResponseEntity.notFound().build();
            }
        } catch (ObjectOptimisticLockingFailureException oe) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "User-Daten nicht aktuell. Bitte neu laden!");
        } catch (Exception e) {
            UserDTO error = new UserDTO();
            error.setErrorMessage(e.getMessage());
            return ResponseEntity.internalServerError().body(error);
        }
    }
}

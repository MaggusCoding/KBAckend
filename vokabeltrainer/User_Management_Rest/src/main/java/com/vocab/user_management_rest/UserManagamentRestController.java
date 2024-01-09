package com.vocab.user_management_rest;

import com.vocab.user_management.entities.UserEntity;
import com.vocab.user_management.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@ComponentScan(basePackages = {"com.vocab"})
public class UserManagamentRestController {
    @Autowired
    private UserService userService;

    @GetMapping("/api/user")
    public ResponseEntity<List<UserEntity>> getAllUsers(){
        try {
            return ResponseEntity.ok(userService.getAll());
        } catch (Exception e){
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/api/user/{id}")
    public ResponseEntity<UserEntity> getUserById(@PathVariable Long id){
        try {
            UserEntity user = userService.getById(id);
            return ResponseEntity.ok(user);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    @PostMapping("/api/user")
    public ResponseEntity<Void> createUser(@RequestBody UserEntity request) throws URISyntaxException {
        try {
            UserEntity user = userService.createUserRest(request);
            URI uri = new URI("/api/user/" + user.getUserId());
            return ResponseEntity.created(uri).build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping("api/user/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id){
        try{
            userService.deleteUser(id);
            return ResponseEntity.ok().build();
        } catch (Exception e){
            return ResponseEntity.notFound().build();
        }
    }
}

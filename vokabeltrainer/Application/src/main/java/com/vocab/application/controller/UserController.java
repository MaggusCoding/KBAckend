package com.vocab.application.controller;

import com.vocab.user_management.entities.UserEntity;
import com.vocab.user_management_impl.services.UserServiceImpl;
import org.springframework.stereotype.Controller;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
@Controller
public class UserController {
    private final UserServiceImpl userService;

    public UserController(UserServiceImpl userService) {
        this.userService = userService;
    }

    public void initializeDefaultUser() {
        String defaultUsername = "god";
        // Überprüfe, ob der Benutzer bereits existiert
        if (userService.findByUsername(defaultUsername).isEmpty()) {
            userService.createUser(defaultUsername);
        }
    }

    public void createUser(Scanner scanner) {
        System.out.println("Enter the username for the new user:");
        scanner.nextLine();  // Consume newline left-over
        String username = scanner.nextLine();
        UserEntity user = userService.createUser(username);
        System.out.println("User created with ID: " + user.getUserId());
    }

    public void deleteUser(Scanner scanner) {
        System.out.println("Available users:");
        List<UserEntity> users = userService.getAll();
        users.forEach(user -> System.out.println(user.getUserId() + " - " + user.getUsername()));

        System.out.println("Enter the ID of the user to delete:");
        try {
            Long userId = scanner.nextLong();
            userService.deleteUser(userId);
            System.out.println("User deleted successfully.");
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Please enter a valid user ID.");
            scanner.next();  // Consume the invalid input
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // Weitere Methoden wie updateUser, getUserById, etc. können hier hinzugefügt werden
}

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

    public Long createUser(Scanner scanner) {
        System.out.println("Enter the username to create the new user or log in as the user:");
        scanner.nextLine();  // Consume newline left-over
        String username = scanner.nextLine();
        UserEntity user = userService.createUser(username);
        System.out.println("Logged in with user ID: " + user.getUserId());
        return user.getUserId();
    }

    public void deleteUser(Scanner scanner, Long loggedInUser) {
        System.out.println("Available users:");
        List<UserEntity> users = userService.getAll().stream().filter(user -> !user.getUserId().equals(loggedInUser)).toList();
        users.forEach(user -> System.out.println(user.getUserId() + " - " + user.getUsername()));

        System.out.println("Enter the ID of the user to delete or '0' to go back:");
        boolean optionInvalid = true;
        Long userId = 0L;
        while (optionInvalid) {
            try {
                userId = scanner.nextLong();
                Long finalUserId = userId;
                if (users.stream().anyMatch(user -> user.getUserId().equals(finalUserId)) || userId.equals(0L)) {
                    optionInvalid = false;
                } else {
                    System.out.println("Entered ID is invalid. Try again!");
                }
            } catch (InputMismatchException e) {
                System.out.println("Entered ID is invalid. Try again!");
                scanner.next();
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
        if (userId > 0) {
            if (userService.deleteUser(userId)) {
                System.out.println("User deleted successfully.");
            } else {
                Long finalUserId1 = userId;
                if(users.stream().anyMatch(user -> user.getUserId().equals(finalUserId1))){
                    System.out.println("User deletion was unsuccessful. The user participates in a duel. Delete the duel first.");
                } else {
                    System.out.println("User deletion was unsuccessful. The user does not exist.");
                }
            }
        }
        // Weitere Methoden wie updateUser, getUserById, etc. können hier hinzugefügt werden
    }
}

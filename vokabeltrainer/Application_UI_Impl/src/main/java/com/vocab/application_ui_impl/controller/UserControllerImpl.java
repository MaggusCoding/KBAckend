package com.vocab.application_ui_impl.controller;

import com.vocab.application_ui_impl.views.UserView;
import com.vocab.user_management.entities.UserEntity;
import com.vocab.user_management_impl.services.UserServiceImpl;
import org.springframework.stereotype.Controller;

import java.util.InputMismatchException;
import java.util.List;

@Controller
public class UserControllerImpl {
    private final UserServiceImpl userService;

    private final UserView userView;

    public UserControllerImpl(UserServiceImpl userService, UserView userView) {
        this.userService = userService;
        this.userView = userView;
    }

    public void initializeDefaultUser() {
        String defaultUsername = "god";
        // Überprüfe, ob der Benutzer bereits existiert
        if (userService.findByUsername(defaultUsername).isEmpty()) {
            userService.createUser(defaultUsername);
        }
    }

    public Long createUser() {
        userView.printCreateInstruction();
        String username = userView.readString();
        UserEntity user = userService.createUser(username);
        userView.printLoginMessage(user.getUserId());
        return user.getUserId();
    }

    public void deleteUser(Long loggedInUser) {
        userView.printAvailableUsers("Available users:");
        List<UserEntity> users = userService.getAll().stream().filter(user -> !user.getUserId().equals(loggedInUser)).toList();
        users.forEach(user -> userView.printAvailableUsers(user.getUserId() + " - " + user.getUsername()));

        userView.printDeleteInstruction();
        boolean optionInvalid = true;
        Long userId = 0L;
        while (optionInvalid) {
            try {
                userId = userView.readLong();
                Long finalUserId = userId;
                if (users.stream().anyMatch(user -> user.getUserId().equals(finalUserId)) || userId.equals(0L)) {
                    optionInvalid = false;
                } else {
                    userView.printInputFailMessage();
                }
            } catch (InputMismatchException e) {
                userView.printInputFailMessage();
                userView.readString();
            } catch (Exception e) {
                userView.printErrorMessage("Error: " + e.getMessage());
            }
        }
        if (userId > 0) {
            if (userService.deleteUser(userId)) {
                userView.printDeletionSuccess();
            } else {
                Long finalUserId1 = userId;
                if(users.stream().anyMatch(user -> user.getUserId().equals(finalUserId1))){
                    userView.printUserExistsInADuel();
                } else {
                    userView.printUserDoesNotExist();
                }
            }
        }
        // Weitere Methoden wie updateUser, getUserById, etc. können hier hinzugefügt werden
    }
}

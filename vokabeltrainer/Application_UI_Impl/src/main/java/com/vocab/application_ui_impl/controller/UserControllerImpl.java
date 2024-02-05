package com.vocab.application_ui_impl.controller;

import com.vocab.application_ui_impl.views.UserView;
import com.vocab.user_management.entities.UserEntity;
import com.vocab.user_management.exceptions.InvalidUsernameException;
import com.vocab.user_management.exceptions.UserNotExistException;
import com.vocab.user_management.exceptions.UserStillPlaysException;
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
        try {
            // Überprüfe, ob der Benutzer bereits existiert
            userService.createUser(defaultUsername);
        }catch(InvalidUsernameException e){
            userView.printUsernameInvalid(e.getMessage());
        }
    }

    public Long createUser() {
        boolean userCreated = false;
        while(!userCreated) {
            userView.printCreateInstruction();
            String username = userView.readString();
            try {
                UserEntity user = userService.createUser(username);
                userView.printLoginMessage(user.getUserId());
                return user.getUserId();
            } catch(InvalidUsernameException e){
                userView.printUsernameInvalid(e.getMessage());
            }
        }
        return null;
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
            try{
                userService.deleteUser(userId);
                userView.printDeletionSuccess();
            } catch(UserNotExistException e){
                userView.printUserDoesNotExist();
            } catch(UserStillPlaysException ex){
                userView.printUserExistsInADuel();
            }
        }
        // Weitere Methoden wie updateUser, getUserById, etc. können hier hinzugefügt werden
    }
}

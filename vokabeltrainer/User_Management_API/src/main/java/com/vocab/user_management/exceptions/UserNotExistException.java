package com.vocab.user_management.exceptions;

public class UserNotExistException extends Exception {

    public UserNotExistException(String message){
        super(message);
    }
}

package com.management.user_management;

public class UserAlreadyExistsException extends Exception{
    public UserAlreadyExistsException(String msg) {
        super(msg);
    }
}

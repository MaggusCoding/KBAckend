package com.vocab.user_management_rest;

import com.vocab.user_management.exceptions.InvalidUsernameException;
import com.vocab.user_management.exceptions.UserNotExistException;
import com.vocab.user_management.exceptions.UserStillPlaysException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class UserRestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    private static final String TECHNICAL_ERROR_MESSAGE = "Es ist ein allgemeiner technischer Fehler aufgetreten.";

    public UserRestResponseEntityExceptionHandler(){
        super();
    }

    // 400
    @ExceptionHandler(value = {InvalidUsernameException.class})
    protected ResponseEntity<Object> handleBadRequestFachlich(InvalidUsernameException ex, WebRequest request){
        return handleExceptionInternal(ex, ex.getMessage(), new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    // 404
    @ExceptionHandler(value = {UserNotExistException.class})
    protected ResponseEntity<Object> handleNotFoundFachlich(UserNotExistException ex, WebRequest request){
        return handleExceptionInternal(ex, ex.getMessage(), new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    // 409
    @ExceptionHandler(value = {ObjectOptimisticLockingFailureException.class})
    protected ResponseEntity<Object> handleConflictTechnisch(ObjectOptimisticLockingFailureException ex, WebRequest request){
        String bodyOfResponse = "User-Daten sind nicht aktuell. Bitte neu laden!";
        return handleExceptionInternal(ex, bodyOfResponse, new HttpHeaders(), HttpStatus.CONFLICT, request);
    }

    @ExceptionHandler(value = {UserStillPlaysException.class})
    protected ResponseEntity<Object> handleConflictFachlich(UserStillPlaysException ex, WebRequest request){
        return handleExceptionInternal(ex, ex.getMessage(), new HttpHeaders(), HttpStatus.CONFLICT, request);
    }

    // 500
    @ExceptionHandler(value = {IllegalArgumentException.class, IllegalStateException.class, NullPointerException.class, RuntimeException.class})
    protected ResponseEntity<Object> handleInternalTechnisch(RuntimeException ex, WebRequest request){
        return handleExceptionInternal(ex, TECHNICAL_ERROR_MESSAGE, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }
}

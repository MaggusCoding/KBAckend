package com.vocab.vocabulary_duel_rest;

import com.vocab.user_management.exceptions.UserNotExistException;
import com.vocab.vocabulary_duel_API.exceptions.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class DuelRestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    private static final String TECHNICAL_ERROR_MESSAGE = "Es ist ein allgemeiner technischer Fehler aufgetreten.";

    public DuelRestResponseEntityExceptionHandler(){
        super();
    }

    // 404
    @ExceptionHandler(value = {UserNotExistException.class})
    protected ResponseEntity<Object> handleNotFoundFachlich(UserNotExistException ex, WebRequest request){
        return handleExceptionInternal(ex, ex.getMessage(), new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(value = {DuelNotExistException.class})
    protected ResponseEntity<Object> handleNotFoundFachlich(DuelNotExistException ex, WebRequest request){
        return handleExceptionInternal(ex, ex.getMessage(), new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(value = {RoundNotExistException.class})
    protected ResponseEntity<Object> handleNotFoundFachlich(RoundNotExistException ex, WebRequest request){
        return handleExceptionInternal(ex, ex.getMessage(), new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    // 409
    @ExceptionHandler(value = {ObjectOptimisticLockingFailureException.class})
    protected ResponseEntity<Object> handleConflictTechnisch(ObjectOptimisticLockingFailureException ex, WebRequest request){
        String bodyOfResponse = "User-Daten sind nicht aktuell. Bitte neu laden!";
        return handleExceptionInternal(ex, bodyOfResponse, new HttpHeaders(), HttpStatus.CONFLICT, request);
    }

    @ExceptionHandler(value = {UserAlreadyPartOfDuelException.class})
    protected ResponseEntity<Object> handleConflictFachlich(UserAlreadyPartOfDuelException ex, WebRequest request){
        return handleExceptionInternal(ex, ex.getMessage(), new HttpHeaders(), HttpStatus.CONFLICT, request);
    }

    @ExceptionHandler(value = {UserAlreadyPlayedRoundException.class})
    protected ResponseEntity<Object> handleConflictFachlich(UserAlreadyPlayedRoundException ex, WebRequest request){
        return handleExceptionInternal(ex, ex.getMessage(), new HttpHeaders(), HttpStatus.CONFLICT, request);
    }

    @ExceptionHandler(value = {UserNotPartOfDuelException.class})
    protected ResponseEntity<Object> handleConflictFachlich(UserNotPartOfDuelException ex, WebRequest request){
        return handleExceptionInternal(ex, ex.getMessage(), new HttpHeaders(), HttpStatus.CONFLICT, request);
    }

    @ExceptionHandler(value = {DuelAlreadyStartedException.class})
    protected ResponseEntity<Object> handleConflictFachlich(DuelAlreadyStartedException ex, WebRequest request){
        return handleExceptionInternal(ex, ex.getMessage(), new HttpHeaders(), HttpStatus.CONFLICT, request);
    }

    // 500
    @ExceptionHandler(value = {IllegalArgumentException.class, IllegalStateException.class, NullPointerException.class, RuntimeException.class})
    protected ResponseEntity<Object> handleInternalTechnisch(RuntimeException ex, WebRequest request){
        return handleExceptionInternal(ex, TECHNICAL_ERROR_MESSAGE, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }
}

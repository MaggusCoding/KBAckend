package com.vocab.vocabulary_management_rest;

import com.vocab.vocabulary_management.exceptions.ContentEmptyException;
import com.vocab.vocabulary_management.exceptions.FlashcardListNotExistException;
import com.vocab.vocabulary_management.exceptions.FlashcardListStillInUseException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.io.IOException;

@ControllerAdvice
public class VocabularyManagementRestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    private static final String TECHNICAL_ERROR_MESSAGE = "Es ist ein allgemeiner technischer Fehler aufgetreten.";

    public VocabularyManagementRestResponseEntityExceptionHandler(){
        super();
    }

    // 400
    @ExceptionHandler(value = {ContentEmptyException.class})
    protected ResponseEntity<Object> handleBadRequestFachlich(ContentEmptyException ex, WebRequest request){
        return handleExceptionInternal(ex, ex.getMessage(), new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    // 404
    @ExceptionHandler(value = {FlashcardListNotExistException.class})
    protected ResponseEntity<Object> handleNotFoundFachlich(FlashcardListNotExistException ex, WebRequest request){
        return handleExceptionInternal(ex, ex.getMessage(), new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    // 409
    @ExceptionHandler(value = {ObjectOptimisticLockingFailureException.class})
    protected ResponseEntity<Object> handleConflictTechnisch(ObjectOptimisticLockingFailureException ex, WebRequest request){
        String bodyOfResponse = "User-Daten sind nicht aktuell. Bitte neu laden!";
        return handleExceptionInternal(ex, bodyOfResponse, new HttpHeaders(), HttpStatus.CONFLICT, request);
    }

    @ExceptionHandler(value = {FlashcardListStillInUseException.class})
    protected ResponseEntity<Object> handleConflictFachlich(FlashcardListStillInUseException ex, WebRequest request){
        return handleExceptionInternal(ex, ex.getMessage(), new HttpHeaders(), HttpStatus.CONFLICT, request);
    }

    // 500
    @ExceptionHandler(value = {IllegalArgumentException.class, IllegalStateException.class, NullPointerException.class, RuntimeException.class, IOException.class})
    protected ResponseEntity<Object> handleInternalTechnisch(RuntimeException ex, WebRequest request){
        return handleExceptionInternal(ex, TECHNICAL_ERROR_MESSAGE, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }
}

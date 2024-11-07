package com.Projeto.Itau.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Void> handleInvalidJsonException() {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<HttpStatus> handleValidationExceptions() {
        return ResponseEntity.unprocessableEntity().build();
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<HttpStatus> handleIllegalArgumentExceptions() {
        return ResponseEntity.unprocessableEntity().build();
    }
}
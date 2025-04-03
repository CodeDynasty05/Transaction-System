package com.matrix.Transaction.exception;

import io.swagger.v3.oas.annotations.Hidden;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
@Hidden
public class GlobalExceptionHandler {
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(Exception ex, HttpServletRequest request) {
        return new ResponseEntity<>(new ErrorResponse(LocalDateTime.now(), HttpStatus.NOT_FOUND.value(), ex.getMessage(), request.getRequestURI()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Forbidden.class)
    public ResponseEntity<ErrorResponse> handleForbiddenException(Exception ex, HttpServletRequest request) {
        return new ResponseEntity<>(new ErrorResponse(LocalDateTime.now(),HttpStatus.FORBIDDEN.value(), ex.getMessage(), request.getRequestURI()), HttpStatus.FORBIDDEN);
    }
}

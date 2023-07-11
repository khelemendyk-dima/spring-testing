package com.my.testing.exceptions.handler;

import com.my.testing.exceptions.*;
import com.my.testing.payload.responses.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Objects;

@Slf4j
@RestControllerAdvice
public class RestResponseEntityExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFoundException(UserNotFoundException e) {
        log.error(e.getMessage());
        return new ResponseEntity<>(new ErrorResponse(
                System.currentTimeMillis(),
                e.getMessage()
        ), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleUserAlreadyExistsException(UserAlreadyExistsException e) {
        log.error(e.getMessage());
        return new ResponseEntity<>(new ErrorResponse(
                System.currentTimeMillis(),
                e.getMessage()
        ), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value = {RefreshTokenExpiredException.class,
            RefreshTokenNotFoundException.class})
    public ResponseEntity<ErrorResponse> handleRefreshTokenException(RuntimeException e) {
        log.error(e.getMessage());
        return new ResponseEntity<>(new ErrorResponse(
                System.currentTimeMillis(),
                e.getMessage()
        ), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(value = {PasswordResetTokenNotFoundException.class,
            PasswordResetTokenExpiredException.class})
    public ResponseEntity<ErrorResponse> handlePasswordResetTokenException(RuntimeException e) {
        log.error(e.getMessage());
        return new ResponseEntity<>(new ErrorResponse(
                System.currentTimeMillis(),
                e.getMessage()
        ), HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error(e.getMessage());
        return new ResponseEntity<>(new ErrorResponse(
                System.currentTimeMillis(),
                Objects.requireNonNull(e.getFieldError()).getDefaultMessage()
        ), HttpStatus.BAD_REQUEST);
    }
}
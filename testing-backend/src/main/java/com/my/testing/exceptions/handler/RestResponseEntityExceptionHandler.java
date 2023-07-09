package com.my.testing.exceptions.handler;

import com.my.testing.dtos.responses.ErrorResponse;
import com.my.testing.exceptions.RefreshTokenExpiredException;
import com.my.testing.exceptions.RefreshTokenNotFoundException;
import com.my.testing.exceptions.UserAlreadyExistsException;
import com.my.testing.exceptions.UserNotFoundException;
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
    public ResponseEntity<ErrorResponse> handleTokenException(RuntimeException e) {
        log.error(e.getMessage());
        return new ResponseEntity<>(new ErrorResponse(
                System.currentTimeMillis(),
                e.getMessage()
        ), HttpStatus.UNAUTHORIZED);
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
package com.devmare.tradingbackend.controller.exceptions;


import com.devmare.tradingbackend.business.domain.DefaultResponse;
import com.devmare.tradingbackend.data.exception.UserInfoException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

import static com.devmare.tradingbackend.business.domain.DefaultResponse.Status.FAILED;


@RestControllerAdvice
public class CustomControllerAdviser {

    @ExceptionHandler(UserInfoException.class)
    public ResponseEntity<DefaultResponse> handleNewUserException(UserInfoException ex) {
        DefaultResponse response = new DefaultResponse(FAILED, ex.getMessage());
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<DefaultResponse> handleInvalidArgumentException(MethodArgumentNotValidException exception) {
        Map<String, String> response = new HashMap<>();
        StringBuilder responseMessage = new StringBuilder();
        exception.getBindingResult().getAllErrors().forEach(objectError -> {
            String fieldName = ((FieldError) objectError).getField();
            String message = objectError.getDefaultMessage();
            responseMessage.append(fieldName).append(" ").append(message).append(", ");
            response.put(fieldName, message);
        });
        return ResponseEntity.status(500).body(new DefaultResponse(FAILED, response, responseMessage.toString()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<DefaultResponse> handleIllegalArgumentException(IllegalArgumentException ex) {
        Map<String, String> response = new HashMap<>();
        return ResponseEntity.status(500).body(new DefaultResponse(FAILED, response, ex.getMessage()));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<DefaultResponse> handleIllegalArgumentException(HttpMessageNotReadableException ex) {
        return ResponseEntity.status(500).body(new DefaultResponse(FAILED, ex.getMessage()));
    }
}
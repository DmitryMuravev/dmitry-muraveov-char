package com.dmitry.muravev.chat.controller;

import com.dmitry.muravev.chat.dto.response.ErrorMessageResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@RestControllerAdvice
public class ChatControllerAdvice {

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ErrorMessageResponse> handleResponseStatusException(ResponseStatusException rse) {
        ErrorMessageResponse errorMessageResponse = ErrorMessageResponse.builder()
                .errorMessage(rse.getReason())
                .build();
        return ResponseEntity.status(rse.getStatus()).body(errorMessageResponse);
    }

}

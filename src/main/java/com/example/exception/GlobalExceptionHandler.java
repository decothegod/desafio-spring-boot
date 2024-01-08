package com.example.exception;


import com.example.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static com.example.util.UtilsServices.covertDateStr;


@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ServiceExceptionBadRequest.class)
    public ResponseEntity<ErrorResponse> handleLoginException(ServiceExceptionBadRequest e) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(covertDateStr(System.currentTimeMillis()))
                .code(HttpStatus.BAD_REQUEST.value())
                .detail(e.getMessage())
                .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ServiceExceptionNotFound.class)
    public ResponseEntity<ErrorResponse> handleLoginException(ServiceExceptionNotFound e) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(covertDateStr(System.currentTimeMillis()))
                .code(HttpStatus.NOT_FOUND.value())
                .detail(e.getMessage())
                .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ServiceExceptionConflict.class)
    public ResponseEntity<ErrorResponse> handleLoginException(ServiceExceptionConflict e) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(covertDateStr(System.currentTimeMillis()))
                .code(HttpStatus.CONFLICT.value())
                .detail(e.getMessage())
                .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);

    }

    @ExceptionHandler(ServiceExceptionUnauthorized.class)
    public ResponseEntity<ErrorResponse> handleLoginException(ServiceExceptionUnauthorized e) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(covertDateStr(System.currentTimeMillis()))
                .code(HttpStatus.UNAUTHORIZED.value())
                .detail(e.getMessage())
                .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }
}

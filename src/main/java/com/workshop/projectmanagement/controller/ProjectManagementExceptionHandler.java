package com.workshop.projectmanagement.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ProjectManagementExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    protected ResponseEntity<ExceptionResponse> handleConflict(IllegalArgumentException ex) {
        ExceptionResponse exceptionResponse = new ExceptionResponse();
        exceptionResponse.setErrorCode("1001");
        exceptionResponse.setErrorMessage(ex.getMessage());

        return ResponseEntity.badRequest().body(exceptionResponse);
    }
}

package com.lmt.Kanban.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

// ApiException.java
@Getter
public class ApiException extends RuntimeException {
    private final HttpStatus status;
    public ApiException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }
}


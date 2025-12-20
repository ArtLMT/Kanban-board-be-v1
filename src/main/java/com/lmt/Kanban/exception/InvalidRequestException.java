package com.lmt.Kanban.exception;

import org.springframework.http.HttpStatus;

// 400 Bad Request
public class InvalidRequestException extends ApiException {
    public InvalidRequestException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}

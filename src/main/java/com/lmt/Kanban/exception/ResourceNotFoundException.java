package com.lmt.Kanban.exception;

import org.springframework.http.HttpStatus;

// 404 Not Found (Dùng chung)
public class ResourceNotFoundException extends ApiException {
    public ResourceNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}

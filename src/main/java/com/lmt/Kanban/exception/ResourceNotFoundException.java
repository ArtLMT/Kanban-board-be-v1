package com.lmt.Kanban.exception;

import org.springframework.http.HttpStatus;

// 404 Not Found (Dùng chung)
public class ResourceNotFoundException extends ApiException {
    public ResourceNotFoundException( ErrorCode code, String message) {
        super(code, message, HttpStatus.NOT_FOUND);
    }
}

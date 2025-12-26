package com.lmt.Kanban.exception;

import org.springframework.http.HttpStatus;

public class ConflictException extends ApiException {
    public ConflictException(ErrorCode code ,String message) {
        super(code, message, HttpStatus.CONFLICT);
    }
}

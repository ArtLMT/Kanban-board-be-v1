package com.lmt.Kanban.exception;

import org.springframework.http.HttpStatus;

public class UnauthenticatedException extends ApiException {
    public UnauthenticatedException(String message) {
        super(ErrorCode.UNAUTHENTICATED,message, HttpStatus.UNAUTHORIZED);
    }
}

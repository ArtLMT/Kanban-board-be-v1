package com.lmt.Kanban.exception;

import org.springframework.http.HttpStatus;

public class TokenNotFoundException extends ApiException {
    public TokenNotFoundException(String message) {
        super(ErrorCode.REFRESH_TOKEN_NOT_FOUND,message, HttpStatus.UNAUTHORIZED);
    }
}

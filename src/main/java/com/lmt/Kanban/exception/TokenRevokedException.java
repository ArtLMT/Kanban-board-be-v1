package com.lmt.Kanban.exception;

import org.springframework.http.HttpStatus;

public class TokenRevokedException extends ApiException {

    public TokenRevokedException(String message) {
        super(ErrorCode.REFRESH_TOKEN_REVOKED, message, HttpStatus.UNAUTHORIZED);
    }
}


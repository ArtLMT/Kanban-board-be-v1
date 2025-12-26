package com.lmt.Kanban.exception;

import org.springframework.http.HttpStatus;

public class TokenExpiredException extends ApiException {
    public TokenExpiredException(String  message) {
        super(ErrorCode.REFRESH_TOKEN_EXPIRED, message, HttpStatus.UNAUTHORIZED);
    }
}

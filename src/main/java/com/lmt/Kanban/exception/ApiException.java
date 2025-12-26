package com.lmt.Kanban.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public abstract class ApiException extends RuntimeException {

    private final HttpStatus status;
    private final ErrorCode code;

    protected ApiException(
            ErrorCode code,
            String message,
            HttpStatus status
    ) {
        super(message);
        this.code = code;
        this.status = status;
    }
}



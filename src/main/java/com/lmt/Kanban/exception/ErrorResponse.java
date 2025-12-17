package com.lmt.Kanban.exception;

import lombok.Data;

@Data
public class ErrorResponse {
    private String message;
    private int status;
    private Long timestamp;
}

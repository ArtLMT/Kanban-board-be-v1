package com.lmt.Kanban.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL) // Chỉ hiện field nào khác null
public class ErrorResponse {
    private int status;
    private String error;
    private String message;
    private String code;
    private LocalDateTime timestamp;
    private Object details; // Dùng cho validation errors hoặc chi tiết bổ sung

    public ErrorResponse(int status, String error, String message, String code) {
        this.status = status;
        this.error = error;
        this.message = message;
        this.code = code;
        this.timestamp = LocalDateTime.now();
    }
}
package com.lmt.Kanban.exception;

public enum ErrorCode {
    /* ===================== AUTH ===================== */
    INVALID_CREDENTIALS,
    REFRESH_TOKEN_EXPIRED,
    REFRESH_TOKEN_REVOKED,
    REFRESH_TOKEN_NOT_FOUND,
    UNAUTHENTICATED,

    /* ===================== USER ===================== */
    USER_NOT_FOUND,
    USERNAME_ALREADY_EXISTS,
    EMAIL_ALREADY_EXISTS,
    USER_NOT_ACTIVE,

    /* ===================== BOARD ===================== */
    BOARD_NOT_FOUND,
    BOARD_ACCESS_DENIED,
    BOARD_ALREADY_DELETED,

    /* ===================== BOARD MEMBER ===================== */
    BOARD_MEMBER_NOT_FOUND,
    BOARD_MEMBER_ALREADY_EXISTS,
    BOARD_MEMBER_ACCESS_DENIED,

    /* ===================== STATUS ===================== */
    STATUS_NOT_FOUND,
    STATUS_NAME_DUPLICATED,
    STATUS_INVALID_POSITION,

    /* ===================== TASK ===================== */
    TASK_NOT_FOUND,
    TASK_ACCESS_DENIED,
    TASK_INVALID_DATA,
    TASK_NAME_DUPLICATED,

    /* ===================== VALIDATION ===================== */
    VALIDATION_FAILED,

    /* ===================== SECURITY ===================== */
    ACCESS_DENIED,

    /* ===================== SYSTEM ===================== */
    INTERNAL_ERROR
}

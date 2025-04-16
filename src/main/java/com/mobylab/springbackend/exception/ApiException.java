package com.mobylab.springbackend.exception;

public class ApiException extends RuntimeException {
    private final ErrorCodes errorCode;

    public ApiException(String message, ErrorCodes errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public ErrorCodes getErrorCode() {
        return errorCode;
    }
}

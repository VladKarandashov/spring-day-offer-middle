package com.onedayoffer.taskdistribution.exception;

import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {

    private final int statusCode;

    public BusinessException(ExceptionStatus status) {
        super(status.getMessage());
        this.statusCode = status.getCode();
    }

    public BusinessException(ExceptionStatus status, String message) {
        super(message);
        this.statusCode = status.getCode();
    }

    @Override
    public String toString() {
        return "BusinessException{" +
                "statusCode=" + getStatusCode() +
                "message=" + getMessage() +
                '}';
    }
}
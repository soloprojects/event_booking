package com.event.booking.exception;

import org.springframework.http.HttpStatus;

public class BusinessException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    private HttpStatus errorCode;
    private String errorMessage;
    public HttpStatus getErrorCode() {
        return errorCode;
    }
    public void setErrorCode(HttpStatus errorCode) {
        this.errorCode = errorCode;
    }
    public String getErrorMessage() {
        return errorMessage;
    }
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
    public static long getSerialversionuid() {
        return serialVersionUID;
    }
    public BusinessException(String errorMessage, HttpStatus errorCode) {
        super();
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public BusinessException() {

    }
}

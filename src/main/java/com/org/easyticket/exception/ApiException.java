package com.org.easyticket.exception;

import org.springframework.http.HttpStatus;

public class ApiException extends RuntimeException{

    public ApiException(String message, String errorCode, HttpStatus httpStatus) {
        super(message);
    }

}
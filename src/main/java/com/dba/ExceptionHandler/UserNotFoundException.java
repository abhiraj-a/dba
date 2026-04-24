package com.dba.ExceptionHandler;

import org.springframework.http.HttpStatus;

public class UserNotFoundException extends ApiException{
    public UserNotFoundException() {
        super("User Not Found", HttpStatus.NOT_FOUND);
    }
}

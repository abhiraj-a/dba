package com.dba.ExceptionHandler;

import org.springframework.http.HttpStatus;

public class ForbiddenException  extends  ApiException{
    public ForbiddenException() {
        super("Access denied", HttpStatus.FORBIDDEN);
    }
}

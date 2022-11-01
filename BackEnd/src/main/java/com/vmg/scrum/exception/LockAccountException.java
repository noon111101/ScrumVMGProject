package com.vmg.scrum.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class LockAccountException extends RuntimeException{
    public LockAccountException(String message) {
        super(message);
    }
}


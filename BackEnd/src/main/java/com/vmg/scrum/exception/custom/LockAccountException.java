package com.vmg.scrum.exception.custom;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public class LockAccountException extends RuntimeException{
    public LockAccountException(String message) {
        super(message);
    }
}


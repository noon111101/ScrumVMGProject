package com.vmg.scrum.exception.custom;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public class UpdateNullException extends RuntimeException{
    public UpdateNullException(String message) {
        super(message);
    }
}

package com.vmg.scrum.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class UpdateNullException extends RuntimeException{
    public UpdateNullException(String message) {
        super(message);
    }
}

package com.vmg.scrum.exception;

public class LockAccountException extends RuntimeException{
    public LockAccountException(String message) {
        super(message);
    }
}

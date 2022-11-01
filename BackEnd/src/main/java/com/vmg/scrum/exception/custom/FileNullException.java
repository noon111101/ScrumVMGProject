package com.vmg.scrum.exception.custom;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FileNullException extends RuntimeException{
    private int code;
    private String message;
    private String field;
    private String objectName;
}

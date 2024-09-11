package com.michelangelo.usermicroservice.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {
    private String name;
    private String field;
    private Object value;

    public ResourceNotFoundException(String name, String field, Object value) {
        super(String.format("%s with '%s' was not found", name, field));
        this.name = name;
        this.field = field;
        this.value = value;
    }

    public String getName() {
        return name;
    }
    public String getField() {
        return field;
    }
    public Object getValue() {
        return value;
    }
}

package com.samrice.readingroomapi.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class RrResourceNotFoundException extends RuntimeException {

    public RrResourceNotFoundException(String message) {
        super(message);
    }
}

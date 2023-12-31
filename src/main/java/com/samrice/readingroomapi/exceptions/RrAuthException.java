package com.samrice.readingroomapi.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class RrAuthException extends RuntimeException {

    public RrAuthException(String message) {
        super(message);
    }
}

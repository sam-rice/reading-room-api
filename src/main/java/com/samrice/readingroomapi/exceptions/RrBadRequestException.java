package com.samrice.readingroomapi.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class RrBadRequestException extends RuntimeException {

    public RrBadRequestException(String message) { super(message); }
}

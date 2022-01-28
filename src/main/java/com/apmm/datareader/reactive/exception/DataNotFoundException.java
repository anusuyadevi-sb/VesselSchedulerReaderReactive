package com.apmm.datareader.reactive.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;

@ResponseStatus( code = HttpStatus.NOT_FOUND)
public class DataNotFoundException extends ResponseStatusException {
    public DataNotFoundException(HttpStatus status, String message){
        super(status,message);
    }


}
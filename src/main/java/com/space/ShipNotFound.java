package com.space;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ShipNotFound extends Exception{
    public ShipNotFound(String message) {
        super(message);
    }
}

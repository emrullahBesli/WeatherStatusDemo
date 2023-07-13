package com.weather.api.weatherStatus.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class CityCouldNotNullException extends RuntimeException {
    public CityCouldNotNullException(String message) {
        super(message);
    }
}

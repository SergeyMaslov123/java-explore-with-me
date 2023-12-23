package ru.practicum.hit;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ValidEx extends RuntimeException {
    public ValidEx(String message) {
        super(message);
    }
}

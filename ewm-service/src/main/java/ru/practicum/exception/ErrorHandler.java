package ru.practicum.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

public class ErrorHandler {
    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleEntity(final EntityNotFoundEx ex) {
        return new ApiError(List.of(Arrays.toString(ex.getStackTrace())),
                ex.getMessage(),
                ex.toString(),
                HttpStatus.NOT_FOUND.toString(),
                LocalDateTime.now());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleValidate(final ValidationEx ex) {
        return new ApiError(List.of(Arrays.toString(ex.getStackTrace())),
                ex.getMessage(),
                ex.toString(),
                HttpStatus.NOT_FOUND.toString(),
                LocalDateTime.now());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleValidate(final ConflictEx ex) {
        return new ApiError(List.of(Arrays.toString(ex.getStackTrace())),
                ex.getMessage(),
                ex.toString(),
                HttpStatus.NOT_FOUND.toString(),
                LocalDateTime.now());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.I_AM_A_TEAPOT)
    public ApiError handleValidate(final Exception ex) {
        return new ApiError(List.of(Arrays.toString(ex.getStackTrace())),
                ex.getMessage(),
                ex.toString(),
                HttpStatus.NOT_FOUND.toString(),
                LocalDateTime.now());
    }

}

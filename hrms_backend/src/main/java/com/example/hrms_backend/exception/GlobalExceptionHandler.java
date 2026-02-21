package com.example.hrms_backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationErrors(MethodArgumentNotValidException ex)
    {
        Map<String, String> errors =new HashMap<>();

        ex.getBindingResult().getFieldErrors()
                .forEach(error ->
                        errors.put(error.getField(),
                                error.getDefaultMessage()));

        return errors;
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, Object> handleNotFound(ResourceNotFoundException ex)
    {
        Map<String, Object> response = new HashMap<>();
        response.put("status", 404);
        response.put("error", "Not Found");
        response.put("message", ex.getMessage());
        return response;
    }

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, Object> handleBadRequest(BadRequestException ex)
    {
        Map<String, Object> response = new HashMap<>();
        response.put("status", 400);
        response.put("error", "Bad Request");
        response.put("message", ex.getMessage());
        return response;
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, Object> handleAll (Exception ex){
        Map<String, Object> response = new HashMap<>();
        response.put("status", 500);
        response.put("error", "Internal Server Error");
        response.put("message", ex.getMessage());
        return response;
    }
}

package com.springboot.demo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;


// what is @RestControllerAdvice?
// = @ControllerAdvice + @ResponseBody
// @ControllerAdvice -> applies to all controller globally (not just one)
// @responseBody -> returns JSON response (not a vew/HTML)
// so: one class hanldes all exceptions from all controllers -> centralized error handling

//@RestController vs @ControllerAdvice?
// @ControllerAdvice -> for MVC apps, returns HTML view
// @RestControllerAdvice -> for REST APIs , returns JSON
@RestControllerAdvice // creates exception for all controllers
public class GlobalExceptionHandler {

    // handles GET /api/user/999 where user doesnt exist -> 404
    // #ExceptionHandler tells Spring: "run this method when ResourceNotFoundException is thrown"
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorDetails> handleResourceNotFoundException(
        ResourceNotFoundException ex, WebRequest request){
            ErrorDetails errorDetails = new ErrorDetails(
                new Date(),
                ex.getMessage(),
                request.getDescription(false)
            );
            // ResponseEntity lets us control both the body and the http status code
            return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
        }

    // catch-all handler - any exception not handled above lands here -> 500 
    // Exception.class is part of all exceptions -> catches everything else
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDetails> hanldeGlobalException(Exception ex, WebRequest request){
        ErrorDetails errorDetails = new ErrorDetails(
            new Date(),
            ex.getMessage(),
            request.getDescription(false)
        );
        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // Day 11 - handles @Valid failure -> 400 bad request
    // MethodAurgumentNot ValidException thrown when @valid fails on @RequestBody
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidation(
        MethodArgumentNotValidException ex){
        
            Map<String, String> errors = new HashMap<>();

            ex.getBindingResult().getAllErrors().forEach(error -> {
                String field = ((FieldError) error).getField();
                String message = error.getDefaultMessage();
                errors.put(field, message);
            });

            return ResponseEntity.badRequest().body(errors);
    }
}

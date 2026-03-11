package com.springboot.demo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

// What is @ResponseStatus?
// Tells spring: "when this exception is thrown, return this HTTP status"
// even without GlobalExceptionHandler, Spring uses this -> returns 404 automatically

// Why extend RuntimeException and not Exception?
// RuntimeException = UNCHECKED -> no need to write "throws" in every method
// Exception = CHECKED -> every method that calls it must declare "throws" = ugly boilerplate

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {
    private String resourceName; // "User"
    private String fieldName; // "id"
    private Object fieldValue; // 999

    public ResourceNotFoundException(String resourceName, String fieldName, Object fieldValue) {
        // this message shows up in the error response JSON
        // "User not found with id: '999' "
        super(String.format("%s not found with %s : '%s'", resourceName, fieldName, fieldValue));
        this.resourceName = resourceName;
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }

    public String getResourceName() {return resourceName; }
    public String getFieldName() {return fieldName; }
    public Object getFieldValue() {return fieldValue; }

}

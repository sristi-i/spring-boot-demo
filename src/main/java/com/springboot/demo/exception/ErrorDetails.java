package com.springboot.demo.exception;

import java.util.Date;

// why do we need ErrorDetails class?
// without it -> Spring returns ugly stack trace (exposes intenal code to client = BAD)
// with it -> returns clean structured JSON: {timestamp, message, details}
// Client only sees what they need to know, nothing about your intenal code

public class ErrorDetails{
    private Date timestamp; // when did error happen
    private String message; // what went wrong - "User not found with id : '999' "
    private String details; // which URL caused it - "uri=/api/users/999"

    public ErrorDetails (Date timestamp, String message, String details){
        this.timestamp = timestamp;
        this.message = message;
        this.details = details;
    }

    // why only getters, no setters?
    // immutable object - once error response is created, it should not be modified
    public Date getTimestamp() {return timestamp;}
    public String getMessage() {return message;}
    public String getDetails() {return details;}

}
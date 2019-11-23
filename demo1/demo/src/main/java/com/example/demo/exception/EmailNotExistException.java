package com.example.demo.exception;

public class EmailNotExistException extends RuntimeException {
    public EmailNotExistException(String message){
        super(message);
    }
}

package com.example.exception;

public class ServiceExceptionConflict extends RuntimeException {
    public ServiceExceptionConflict(String msg) {
        super(msg);
    }
}

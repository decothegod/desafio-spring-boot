package com.example.exception;

public class ServiceExceptionUnauthorized extends RuntimeException {
    public ServiceExceptionUnauthorized(String msg) {
        super(msg);
    }
}

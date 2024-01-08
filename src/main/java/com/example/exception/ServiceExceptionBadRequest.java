package com.example.exception;

public class ServiceExceptionBadRequest extends RuntimeException {
    public ServiceExceptionBadRequest(String msg) {
        super(msg);
    }
}

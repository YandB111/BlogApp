package com.yb.blogApp.response;

import org.springframework.http.HttpStatus;

public class GenericResponse<T> {
    private T data;
    private String message;
    private HttpStatus status;

    public GenericResponse() {
    }

    public GenericResponse(T data, String message, HttpStatus status) {
        this.data = data;
        this.message = message;
        this.status = status;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }
}

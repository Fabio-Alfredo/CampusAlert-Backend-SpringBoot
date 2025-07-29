package com.kafka.userservice.domain.dtos.commons;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class GeneralResponse {

    private String message;
    private Object data;

    public GeneralResponse(String message, Object data) {
        this.message = message;
        this.data = data;
    }
    public GeneralResponse() {

    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public static ResponseEntity<GeneralResponse>getResponse(HttpStatus status, String message, Object data){
        return new ResponseEntity<>(
                new GeneralResponse(message, data),
                status
        );
    }

    public static ResponseEntity<GeneralResponse>getResponse(HttpStatus status, String message){
        return getResponse(status, message, null);
    }

    public static ResponseEntity<GeneralResponse>getResponse(HttpStatus status, Object data){
        return getResponse(status, status.getReasonPhrase(), data);
    }
}

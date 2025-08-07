package com.kafka.incidentservice.domain.dtos.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GeneralResponse {
    private String message;
    private Object data;

    public static ResponseEntity<GeneralResponse>getResponse(HttpStatus status, String message, Object data){
        GeneralResponse response = new GeneralResponse(message, data);
        return new ResponseEntity<>(response, status);
    }

    public static ResponseEntity<GeneralResponse>getResponse(HttpStatus status, String message){
        return getResponse(status, message, null);
    }

    public static ResponseEntity<GeneralResponse>getResponse(HttpStatus status, Object data){
        return getResponse(status, status.getReasonPhrase(), data);
    }
}

package com.kafka.incidentservice.uitls.error;

import org.springframework.stereotype.Component;
import org.springframework.validation.FieldError;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ErrorTools {

    public Map<String, List<String>>errorMap(List<FieldError>errors){
        Map<String, List<String>> mapError = new HashMap<>();

        errors.forEach(e -> {
                    List<String> _errors = mapError.getOrDefault(e.getField(), new ArrayList<>());
                    _errors.add(e.getDefaultMessage());
                    mapError.put(e.getField(), _errors);

        });
        return mapError;
    }
}

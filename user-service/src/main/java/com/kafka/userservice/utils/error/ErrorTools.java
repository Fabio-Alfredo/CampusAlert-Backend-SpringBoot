package com.kafka.userservice.utils.error;

import org.springframework.stereotype.Component;
import org.springframework.validation.FieldError;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ErrorTools {

    public Map<String, List<String>>mapErrors(List<FieldError>errors){
        Map<String, List<String>>errorMap = new HashMap<>();

        errors.forEach(e->{
            List<String>_errors = errorMap.getOrDefault(e.getField(), new ArrayList<>());

            _errors.add(e.getDefaultMessage());
            errorMap.put(e.getField(), _errors);
        });
        return errorMap;
    }
}

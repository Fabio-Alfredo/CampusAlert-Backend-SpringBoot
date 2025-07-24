package com.kafka.userservice.domain.enums;


public enum TypeToken {
    AUTH_TOKEN,
    RESET_PASSWORD_TOKEN;

    public static TypeToken fromString(String type){
        for(TypeToken typeToken: TypeToken.values()){
            if(typeToken.name().equalsIgnoreCase(type))
                return typeToken;
        }
        throw new IllegalArgumentException("Typo de token invalido");
    }
}

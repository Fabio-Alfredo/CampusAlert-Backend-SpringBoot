package com.kafka.userservice.domain.enums;

public enum AuthProvider {
    GOOGLE_PROVIDER,
    LOCAL;

    public static AuthProvider fromString(String action){
        for(AuthProvider authProvider: AuthProvider.values()){
            if(authProvider.name().equalsIgnoreCase(action))
                return authProvider;
        }
        throw new IllegalArgumentException("No existe el metodo de autenticacion solicitado");
    }

}

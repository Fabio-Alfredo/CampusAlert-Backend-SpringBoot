package com.kafka.userservice.utils.token.factory;

import com.kafka.userservice.domain.enums.TypeToken;
import com.kafka.userservice.utils.token.provider.contract.ITokenProvider;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class TokenProviderFactory {

    private final Map<TypeToken, ITokenProvider> providers;

    public TokenProviderFactory(List<ITokenProvider>providers) {
        this.providers = providers.stream()
                .collect(Collectors.toMap(ITokenProvider::supportedTypeToken, p->p));
    }

    public ITokenProvider of(TypeToken typeToken){
        ITokenProvider tokenProvider = providers.get(typeToken);
        if(tokenProvider == null){
            throw new IllegalArgumentException("Tipo de proveedor de tokens invalido: "+ typeToken);
        }
        return tokenProvider;
    }
}

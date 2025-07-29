package com.kafka.userservice.services.contract;

public interface EmailService {
    void sendRecoveryEmail(String to, String token);
}

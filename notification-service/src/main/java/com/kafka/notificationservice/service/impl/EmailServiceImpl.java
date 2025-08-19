package com.kafka.notificationservice.service.impl;

import com.kafka.notificationservice.service.contract.EmailService;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender javaMailSender;

    public EmailServiceImpl(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Override
    public void incidentReportedSendEmail(String to, UUID incidentId) {
        try{
            String subject = "Incidente registrado con exito - Notification Service";
            String body = """
                Hola,

                Tu incidente con ID %s ha sido registrado exitosamente.
                Nuestro equipo lo revisará a la brevedad.

                Atentamente,
                El equipo de soporte.
                """.formatted(incidentId);
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject(subject);
            message.setText(body);

            javaMailSender.send(message);
        }catch (Exception e){
            throw new RuntimeException("Error sending incident reported email: " + e.getMessage());
        }
    }

    @Override
    public void incidentAssignedSendEmail(String to, String incidentId) {

    }

    @Override
    public void incidentStatusUpdatedSendEmail(String to, String incidentId, String status) {

    }

    @Override
    public void passwordResetSendEmail(String to, String token) {

    }
}

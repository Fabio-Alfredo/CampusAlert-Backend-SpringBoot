package com.kafka.notificationservice.service.contract;

import java.util.UUID;

public interface EmailService {
    void incidentReportedSendEmail(String to, UUID incidentId);
    void incidentAssignedSendEmail(String to, String incidentId);
    void incidentStatusUpdatedSendEmail(String to, String incidentId, String status);
    void passwordResetSendEmail(String to, String token);
}

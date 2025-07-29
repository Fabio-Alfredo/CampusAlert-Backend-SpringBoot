package com.kafka.auditservice.services.contract;

import com.kafka.auditservice.domain.dtos.audit.UserAuditDto;
import com.kafka.auditservice.domain.models.Audit;

import java.util.List;

public interface AuditService {
    void createAudit(UserAuditDto userAuditDto);
}

package com.kafka.auditservice.repositories;

import com.kafka.auditservice.domain.enums.EventType;
import com.kafka.auditservice.domain.models.Audit;
import com.kafka.auditservice.services.contract.AuditService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AuditRepository extends JpaRepository<Audit, UUID> {

    List<Audit>findAllBySourceService(String source_service);

}

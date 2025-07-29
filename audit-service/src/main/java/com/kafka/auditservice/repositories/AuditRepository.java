package com.kafka.auditservice.repositories;

import com.kafka.auditservice.domain.models.Audit;
import com.kafka.auditservice.services.contract.AuditService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AuditRepository extends JpaRepository<Audit, UUID> {

}

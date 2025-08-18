package com.kafka.incidentservice.Repositories;

import com.kafka.incidentservice.domain.models.Incident;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface IncidentRepository extends JpaRepository<Incident, UUID> {
    List<Incident>findAllByReportedBy(UUID reportedBy);
}

package com.kafka.incidentservice.domain.enums;

public enum IncidentStatus {
    REPORTED,
    IN_PROGRESS,
    RESOLVED;

    public static IncidentStatus fromString(String status){
        for(IncidentStatus incident: IncidentStatus.values()){
            if(incident.name().equalsIgnoreCase(status)){
                return incident;
            }
        }

        throw new IllegalArgumentException("Unknown incident status: " + status);
    }
}

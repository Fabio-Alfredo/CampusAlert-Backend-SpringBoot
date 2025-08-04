package com.kafka.incidentservice.domain.enums;

public enum IncidentType {
    TECHNICAL,
    SECURITY,
    MEDICAL,
    ENVIRONMENTAL,
    OTHER;

    public static IncidentType fromString(String type){
        for (IncidentType incident: IncidentType.values()){
            if(incident.name().equalsIgnoreCase(type)){
                return incident;
            }
        }
        throw new IllegalArgumentException("Unknown incident type: " + type);
    }


}

package com.example.loader.service;

import com.example.loader.dto.IncidentDto;
import com.example.loader.model.Incident;
import com.example.loader.repository.IncidentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class IncidentService {
    private final IncidentRepository incidentRepository;

    public List<IncidentDto> findByLoaderId(Long loaderId) {
        List<Incident> incidents = incidentRepository.findByLoaderIdOrderByStartTimeDesc(loaderId);
        return incidents.stream().map(this::toDto).collect(Collectors.toList());
    }

    @Transactional
    public IncidentDto create(IncidentDto dto) {
        Incident incident = new Incident();
        incident.setLoaderId(dto.getLoaderId());
        incident.setStartTime(dto.getStartTime() != null ? dto.getStartTime() : LocalDateTime.now());
        incident.setEndTime(dto.getEndTime());
        incident.setDescription(dto.getDescription());
        Incident saved = incidentRepository.save(incident);
        return toDto(saved);
    }

    @Transactional
    public IncidentDto update(Long id, IncidentDto dto) {
        Incident existing = incidentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Инцидент не найден"));
        existing.setStartTime(dto.getStartTime());
        existing.setEndTime(dto.getEndTime());
        existing.setDescription(dto.getDescription());
        Incident updated = incidentRepository.save(existing);
        return toDto(updated);
    }

    @Transactional
    public void delete(Long id) {
        incidentRepository.deleteById(id);
    }

    private IncidentDto toDto(Incident incident) {
        IncidentDto dto = new IncidentDto();
        dto.setId(incident.getId());
        dto.setLoaderId(incident.getLoaderId());
        dto.setStartTime(incident.getStartTime());
        dto.setEndTime(incident.getEndTime());
        dto.setDescription(incident.getDescription());
        LocalDateTime end = incident.getEndTime() != null ? incident.getEndTime() : LocalDateTime.now();
        Duration duration = Duration.between(incident.getStartTime(), end);
        long hours = duration.toHours();
        long minutes = duration.toMinutes() % 60;
        dto.setDowntime(String.format("%dч %dм", hours, minutes));
        return dto;
    }
}
package com.example.loader.controller;

import com.example.loader.dto.IncidentDto;
import com.example.loader.repository.IncidentRepository;
import com.example.loader.service.IncidentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/loaders/{loaderId}/incidents")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class IncidentController {
    private final IncidentService incidentService;

    @GetMapping
    public List<IncidentDto> getIncidents(@PathVariable Long loaderId) {
        return incidentService.findByLoaderId(loaderId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public IncidentDto create(@PathVariable Long loaderId, @Valid @RequestBody IncidentDto dto) {
        dto.setLoaderId(loaderId);
        return incidentService.create(dto);
    }

    @PutMapping("/{incidentId}")
    public IncidentDto update(@PathVariable Long loaderId,
                              @PathVariable Long incidentId,
                              @Valid @RequestBody IncidentDto dto) {
        dto.setLoaderId(loaderId);
        return incidentService.update(incidentId, dto);
    }

    @DeleteMapping("/{incidentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long loaderId, @PathVariable Long incidentId) {
        incidentService.delete(incidentId);
    }

}
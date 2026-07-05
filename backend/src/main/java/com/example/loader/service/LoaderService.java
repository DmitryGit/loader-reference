package com.example.loader.service;

import com.example.loader.dto.LoaderDto;
import com.example.loader.model.Loader;
import com.example.loader.repository.LoaderRepository;
//import com.example.loader.repository.IncidentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LoaderService {
    private final LoaderRepository loaderRepository;
    //private final IncidentRepository incidentRepository;

    public List<LoaderDto> findAll(String number) {
        List<Loader> loaders = (number == null || number.isEmpty())
                ? (List<Loader>) loaderRepository.findAll()
                : loaderRepository.findByNumberContainingIgnoreCase(number);
        return loaders.stream().map(this::toDto).collect(Collectors.toList());
    }

    @Transactional
    public LoaderDto create(LoaderDto dto, String username) {
        Loader loader = new Loader();
        loader.setBrand(dto.getBrand());
        loader.setNumber(dto.getNumber());
        loader.setCapacity(dto.getCapacity());
        loader.setActive(dto.getActive() != null ? dto.getActive() : true);
        loader.setLastModified(LocalDateTime.now());
        loader.setModifiedBy(username);
        Loader saved = loaderRepository.save(loader);
        return toDto(saved);
    }

    @Transactional
    public LoaderDto update(Long id, LoaderDto dto, String username) {
        Loader existing = loaderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Погрузчик не найден"));
        existing.setBrand(dto.getBrand());
        existing.setNumber(dto.getNumber());
        existing.setCapacity(dto.getCapacity());
        existing.setActive(dto.getActive());
        existing.setLastModified(LocalDateTime.now());
        existing.setModifiedBy(username);
        Loader updated = loaderRepository.save(existing);
        return toDto(updated);
    }

    @Transactional
    public void delete(Long id) {
        if (loaderRepository.hasIncidents(id)) {
            throw new RuntimeException("Удаление запрещено: для погрузчика зарегистрированы простои");
        }
        loaderRepository.deleteById(id);
    }

    private LoaderDto toDto(Loader loader) {
        LoaderDto dto = new LoaderDto();
        dto.setId(loader.getId());
        dto.setBrand(loader.getBrand());
        dto.setNumber(loader.getNumber());
        dto.setCapacity(loader.getCapacity());
        dto.setActive(loader.getActive());
        dto.setLastModified(loader.getLastModified() != null ? loader.getLastModified().toString() : null);
        dto.setModifiedBy(loader.getModifiedBy());
        return dto;
    }
}
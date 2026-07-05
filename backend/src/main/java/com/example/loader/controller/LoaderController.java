package com.example.loader.controller;

import com.example.loader.dto.LoaderDto;
import com.example.loader.service.LoaderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/loaders")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class LoaderController {
    private final LoaderService loaderService;

    @GetMapping
    public List<LoaderDto> getAll(@RequestParam(required = false) String number) {
        return loaderService.findAll(number);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public LoaderDto create(@Valid @RequestBody LoaderDto dto) {
        return loaderService.create(dto, "Петров Ю.А.");
    }

    @PutMapping("/{id}")
    public LoaderDto update(@PathVariable Long id, @Valid @RequestBody LoaderDto dto) {
        return loaderService.update(id, dto, "Иванов С.П.");
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        loaderService.delete(id);
    }
}
package com.example.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.domain.Obra;
import com.example.dto.ObraDTO;
import com.example.services.ObraService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/obras")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ObraController {

    private final ObraService obraService;

    @PostMapping
    public ResponseEntity<Obra> criarObra(@Valid @RequestBody ObraDTO dto) {
        return new ResponseEntity<>(obraService.criarObra(dto), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Obra>> listarObras() {
        return ResponseEntity.ok(obraService.listarTodas());
    }
}
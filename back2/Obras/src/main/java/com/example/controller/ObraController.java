package com.example.controller;

import java.math.BigDecimal;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.domain.Obra;
import com.example.dto.ObraDTO;
import com.example.services.ObraService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/obras")
@RequiredArgsConstructor
public class ObraController {
    
    private final ObraService obraService;

    @PostMapping
    public ResponseEntity<Obra> criarObra(@RequestBody ObraDTO dto) {
        return new ResponseEntity<>(obraService.criarObra(dto), HttpStatus.CREATED);
    }

    @GetMapping("/{id}/saldo")
    public ResponseEntity<BigDecimal> getSaldo(@PathVariable Long id) {
        return ResponseEntity.ok(obraService.calcularSaldoDisponivel(id));
    }
}
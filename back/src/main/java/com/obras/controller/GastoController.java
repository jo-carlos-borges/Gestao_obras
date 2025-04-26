package com.obras.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/gastos")
@RequiredArgsConstructor
public class GastoController {
    
    private final GastoService gastoService;

    @PostMapping
    public ResponseEntity<Gasto> createGasto(@RequestBody GastoDTO gastoDTO) {
        Gasto novoGasto = gastoService.createGasto(gastoDTO);
        return new ResponseEntity<>(novoGasto, HttpStatus.CREATED);
    }

    @GetMapping("/obra/{obraId}")
    public List<Gasto> getGastosByObra(@PathVariable Long obraId) {
        return gastoService.getGastosByObra(obraId);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGasto(@PathVariable Long id) {
        gastoService.deleteGasto(id);
        return ResponseEntity.noContent().build();
    }
}

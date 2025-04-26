package com.obras.controller;

import java.awt.print.Pageable;

import org.hibernate.query.Page;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties.Sort;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.obras.services.ObraService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/obras")
@RequiredArgsConstructor
public class ObraController {
    
    private final ObraService obraService;

    @PostMapping
    public ResponseEntity<Obra> createObra(@RequestBody ObraDTO obraDTO) {
        Obra novaObra = obraService.createObra(obraDTO);
        return new ResponseEntity<>(novaObra, HttpStatus.CREATED);
    }

 // ObraController.java (controller)
    @GetMapping
    public ResponseEntity<Page<ObraResponse>> listarObras(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size,
        @RequestParam(defaultValue = "nome") String sort,
        @RequestParam(required = false) StatusObra status) {
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort));
        Page<Obra> obras = obraService.listarObrasPaginadas(status, pageable);
        Page<ObraResponse> response = obras.map(ObraMapper::toResponse);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/concluir")
    public ResponseEntity<Obra> concluirObra(@PathVariable Long id) {
        return ResponseEntity.ok(obraService.concluirObra(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteObra(@PathVariable Long id) {
        obraService.deleteObra(id);
        return ResponseEntity.noContent().build();
    }
}
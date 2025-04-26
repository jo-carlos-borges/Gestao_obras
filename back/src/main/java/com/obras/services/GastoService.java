package com.obras.services;

import org.springframework.stereotype.Service;

import com.obras.repositories.GastoRepository;
import com.obras.repositories.ObraRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GastoService {
    
    private final GastoRepository gastoRepository;
    private final ObraRepository obraRepository;

    public Gasto createGasto(GastoDTO gastoDTO) {
        Obra obra = obraRepository.findById(gastoDTO.getObraId()).orElseThrow();
        
        Gasto gasto = new Gasto();
        // Mapear DTO para entidade
        gasto.setObra(obra);
        
        return gastoRepository.save(gasto);
    }
}
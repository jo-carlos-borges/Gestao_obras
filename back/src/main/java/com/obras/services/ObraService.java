package com.obras.services;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.springframework.stereotype.Service;

import com.obras.repositories.ObraRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ObraService {
    
    private final ObraRepository obraRepository;

    public Obra createObra(ObraDTO obraDTO) {
        Obra obra = new Obra();
        // Mapear DTO para entidade
        return obraRepository.save(obra);
    }

    public Obra concluirObra(Long id) {
        Obra obra = obraRepository.findById(id).orElseThrow();
        obra.setStatus(StatusObra.CONCLUIDA);
        obra.setDataFim(LocalDate.now());
        return obraRepository.save(obra);
    }
}

public ObraResumoDTO calcularResumoObra(Long obraId) {
    Obra obra = obraRepository.findByIdWithGastos(obraId)
        .orElseThrow(() -> new ResourceNotFoundException("Obra não encontrada"));
    
    BigDecimal totalGastos = obra.getGastos().stream()
        .map(Gasto::getValor)
        .reduce(BigDecimal.ZERO, BigDecimal::add);
    
    BigDecimal saldoDisponivel = obra.getValorTotal().subtract(totalGastos);
    BigDecimal lucro = saldoDisponivel.subtract(obra.getCustoEstimado());
    
    return new ObraResumoDTO(
        totalGastos,
        saldoDisponivel,
        lucro,
        obra.getGastos().size()
    );
}

package com.example.services;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import com.example.domain.Gasto;
import com.example.domain.Obra;
import com.example.dto.ObraDTO;
import com.example.repositories.ObraRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ObraService {
    
    private final ObraRepository obraRepository;

    public Obra criarObra(ObraDTO dto) {
        Obra obra = new Obra();
        obra.setNome(dto.nome());
        obra.setDescricao(dto.descricao());
        obra.setDataInicio(dto.dataInicio());
        obra.setValorTotal(dto.valorTotal());
        return obraRepository.save(obra);
    }
    
    public BigDecimal calcularSaldoDisponivel(Long obraId) {
        Obra obra = obraRepository.findById(obraId).orElseThrow();
        BigDecimal totalGastos = obra.getGastos().stream()
            .map(Gasto::getValor)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        return obra.getValorTotal().subtract(totalGastos);
    }
}
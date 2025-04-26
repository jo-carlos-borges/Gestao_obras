package com.example.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.domain.Cliente;
import com.example.domain.Obra;
import com.example.dto.ObraDTO;
import com.example.repositories.ClienteRepository;
import com.example.repositories.ObraRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ObraService {
    
    private final ObraRepository obraRepository;
    private final ClienteRepository clienteRepository;

    @Transactional
    public Obra criarObra(ObraDTO dto) {
        Cliente cliente = clienteRepository.findById(dto.clienteId())
            .orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado"));

        Obra obra = new Obra();
        obra.setNome(dto.nome());
        obra.setDescricao(dto.descricao());
        obra.setDataInicio(dto.dataInicio());
        obra.setValorTotal(dto.valorTotal());
        obra.setCliente(cliente);

        return obraRepository.save(obra);
    }

    public List<Obra> listarTodas() {
        return obraRepository.findAll();
    }
}
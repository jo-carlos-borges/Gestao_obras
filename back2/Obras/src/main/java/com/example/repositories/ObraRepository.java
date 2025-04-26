package com.example.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.domain.Obra;

public interface ObraRepository extends JpaRepository<Obra, Long> {
    List<Obra> findByClienteId(Long clienteId);
    List<Obra> findByStatus(Obra.StatusObra status);
}
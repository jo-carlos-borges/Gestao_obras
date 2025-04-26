package com.obras.repositories;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface GastoRepository extends JpaRepository<Gasto, Long> {
    List<Gasto> findByObraId(Long obraId);
    List<Gasto> findByObraIdAndDataBetween(Long obraId, LocalDate start, LocalDate end);
}

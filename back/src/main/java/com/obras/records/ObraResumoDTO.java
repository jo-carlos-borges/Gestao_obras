package com.obras.records;

import java.math.BigDecimal;
import java.time.LocalDate;

//records/ObraResumoDTO.java
public record ObraResumoDTO(
 BigDecimal totalGastos,
 BigDecimal saldoDisponivel,
 BigDecimal lucro,
 int totalGastosRegistrados
) {}

//records/ObraFiltro.java
public record ObraFiltro(
 String nome,
 LocalDate dataInicioInicial,
 LocalDate dataInicioFinal,
 StatusObra status
) {}

package com.example.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ObraDTO(
	    String nome,
	    String descricao,
	    LocalDate dataInicio,
	    BigDecimal valorTotal
	) {}

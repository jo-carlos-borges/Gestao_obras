package com.example.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record ObraDTO(
	    @NotBlank String nome,
	    String descricao,
	    @NotNull LocalDate dataInicio,
	    @NotNull @Positive BigDecimal valorTotal,
	    @NotNull Long clienteId
) {}
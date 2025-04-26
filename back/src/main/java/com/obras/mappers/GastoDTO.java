package com.obras.mappers;

@Getter
@Setter
public class GastoDTO {
    private LocalDate data;
    private BigDecimal valor;
    private String descricao;
    private Long obraId;
}
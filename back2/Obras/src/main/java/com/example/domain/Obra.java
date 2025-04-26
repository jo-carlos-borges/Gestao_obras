package com.example.domain;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity

public class Obra {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable = false)
	private String nome;
	private String descricao;
	
	@Column(nullable = false)
	private LocalDate dataInicio;
	private LocalDate dataFim;
	
	@Column(nullable = false, precision = 15, scale = 2)
	private BigDecimal valorTotal;
	    
    @Enumerated(EnumType.STRING)
    private StatusObra status = StatusObra.EM_ANDAMENTO;
	    
    @OneToMany(mappedBy = "obra", cascade = CascadeType.ALL)
    private List<Gasto> gastos = new ArrayList<>();
	    
    public enum StatusObra {
        EM_ANDAMENTO, CONCLUIDA
    }
}

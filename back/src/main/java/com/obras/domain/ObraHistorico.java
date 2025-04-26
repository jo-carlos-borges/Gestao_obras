package com.obras.domain;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class ObraHistorico {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    private Obra obra;
    
    private String campoAlterado;
    private String valorAnterior;
    private String valorNovo;
    private LocalDateTime dataAlteracao;
    private String usuario;
}

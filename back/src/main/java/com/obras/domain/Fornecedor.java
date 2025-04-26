package com.obras.domain;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.validator.constraints.br.CNPJ;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = "cnpj"))
public class Fornecedor extends AbstractAuditableEntity {
 @Id
 @GeneratedValue(strategy = GenerationType.IDENTITY)
 private Long id;
 
 @NotBlank
 private String nome;
 
 @NotBlank
 @CNPJ
 private String cnpj;
 
 @OneToMany(mappedBy = "fornecedor", cascade = CascadeType.ALL)
 private List<Gasto> gastos = new ArrayList<>();
}
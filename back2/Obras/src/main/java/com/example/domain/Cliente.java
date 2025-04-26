package com.example.domain;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity

public class Cliente {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotBlank(message = "Nome é Obrigatorio")
	private String nome;
	
	@NotBlank(message = "CPF/CNPJ é obrigatorio")
	@Column(unique = true)
	private String cpfCnpj;
	private String telfone;
	
	@Email(message = "Email Invalido")
	private String email;
	
	@Embedded
    private Endereco endereco;
	
	@OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL)
    private List<Obra> obras = new ArrayList<>();
}

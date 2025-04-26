package com.example.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record ClienteDTO(
	    @NotBlank String nome,
	    @NotBlank String cpfCnpj,
	    String telefone,
	    @Email String email,
	    String logradouro,
	    String numero,
	    String complemento,
	    String cidade,
	    String estado,
	    String cep
	) {}
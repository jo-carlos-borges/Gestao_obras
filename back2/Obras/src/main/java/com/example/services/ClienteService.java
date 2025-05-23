package com.example.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.domain.Cliente;
import com.example.domain.Endereco;
import com.example.dto.ClienteDTO;
import com.example.repositories.ClienteRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ClienteService {
    
	private final ClienteRepository clientRepository;
	
	@Transactional
	public Cliente criarCliente(ClienteDTO dto) {
		if(ClienteRepository.existsByCpfCnpj(dto.cpfCnpj())) {
			throw new IllegalArgumentException("CPF/CNPJ já cadastrado");
		}
		
		Endereco endereco = new Endereco();
		endereco.setLogradouro(dto.logradouro());
        endereco.setNumero(dto.numero());
        endereco.setComplemento(dto.complemento());
        endereco.setBairro(dto.bairro());
        endereco.setCidade(dto.cidade());
        endereco.setEstado(dto.estado());
        endereco.setCep(dto.cep());

        Cliente cliente = new Cliente();
        cliente.setNome(dto.nome());
        cliente.setCpfCnpj(dto.cpfCnpj());
        cliente.setTelefone(dto.telefone());
        cliente.setEmail(dto.email());
        cliente.setEndereco(endereco);

        return clienteRepository.save(cliente);
	}
	
   public List<Cliente> listarTodos() {
        return ClienteRepository.findAll();
    }
}
package com.farmacia.system.service;

import com.farmacia.system.dto.ClienteDTO;
import com.farmacia.system.entity.Cliente;
import com.farmacia.system.exception.BusinessException;
import com.farmacia.system.exception.ResourceNotFoundException;
import com.farmacia.system.repository.ClienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClienteService {

    private final ClienteRepository clienteRepository;
    private final AuditService auditService;

    public ClienteDTO criar(ClienteDTO dto) {
        if (clienteRepository.existsByCpf(dto.getCpf())) {
            throw new BusinessException("CPF já cadastrado.");
        }
        if (clienteRepository.existsByEmail(dto.getEmail())) {
            throw new BusinessException("Email já cadastrado.");
        }

        Cliente cliente = Cliente.builder()
                .nome(dto.getNome())
                .cpf(dto.getCpf())
                .email(dto.getEmail())
                .dataNascimento(dto.getDataNascimento())
                .telefone(dto.getTelefone())
                .endereco(dto.getEndereco())
                .build();

        if (dto.getDataNascimento() != null && !cliente.isMaiorDeIdade()) {
            throw new BusinessException("Cliente deve ser maior de 18 anos.");
        }

        Cliente salvo = clienteRepository.save(cliente);
        auditService.log("CLIENTE", "Criado cliente: " + salvo.getCpf());
        return mapToDTO(salvo);
    }

    public ClienteDTO atualizar(UUID id, ClienteDTO dto) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado."));

        if (!cliente.getCpf().equals(dto.getCpf()) && clienteRepository.existsByCpf(dto.getCpf())) {
            throw new BusinessException("CPF já em uso.");
        }

        cliente.setNome(dto.getNome());
        cliente.setEmail(dto.getEmail());
        cliente.setDataNascimento(dto.getDataNascimento());
        cliente.setTelefone(dto.getTelefone());
        cliente.setEndereco(dto.getEndereco());

        Cliente salvo = clienteRepository.save(cliente);
        auditService.log("CLIENTE", "Atualizado cliente: " + salvo.getCpf());
        return mapToDTO(salvo);
    }

    public List<ClienteDTO> listar() {
        return clienteRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public ClienteDTO buscarPorId(UUID id) {
        Cliente c = clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado."));
        return mapToDTO(c);
    }

    private ClienteDTO mapToDTO(Cliente c) {
        ClienteDTO dto = new ClienteDTO();
        dto.setId(c.getId());
        dto.setNome(c.getNome());
        dto.setCpf(c.getCpf());
        dto.setEmail(c.getEmail());
        dto.setDataNascimento(c.getDataNascimento());
        dto.setTelefone(c.getTelefone());
        dto.setEndereco(c.getEndereco());
        return dto;
    }
}

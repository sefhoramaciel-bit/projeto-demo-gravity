package com.farmacia.system.service;

import com.farmacia.system.dto.CategoriaDTO;
import com.farmacia.system.entity.Categoria;
import com.farmacia.system.exception.BusinessException;
import com.farmacia.system.exception.ResourceNotFoundException;
import com.farmacia.system.repository.CategoriaRepository;
import com.farmacia.system.repository.MedicamentoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;
    private final MedicamentoRepository medicamentoRepository;
    private final AuditService auditService;

    public CategoriaDTO criar(CategoriaDTO dto) {
        if (categoriaRepository.existsByNome(dto.getNome())) {
            throw new BusinessException("Categoria já existe.");
        }
        Categoria categoria = new Categoria();
        categoria.setNome(dto.getNome());
        categoria.setDescricao(dto.getDescricao());
        Categoria salvo = categoriaRepository.save(categoria);

        auditService.log("CATEGORIA", "Criada categoria: " + salvo.getNome());

        return mapToDTO(salvo);
    }

    public List<CategoriaDTO> listar() {
        return categoriaRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public CategoriaDTO buscarPorId(UUID id) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria não encontrada."));
        return mapToDTO(categoria);
    }

    // Only used internally if needed
    public Categoria findEntityById(UUID id) {
        return categoriaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria não encontrada (ID: " + id + ")"));
    }

    public void deletar(UUID id) {
        if (medicamentoRepository.countByCategoriaId(id) > 0) {
            throw new BusinessException("Não é possível excluir categoria com medicamentos vinculados.");
        }
        Categoria categoria = findEntityById(id);
        categoriaRepository.delete(categoria);
        auditService.log("CATEGORIA", "Deletada categoria: " + categoria.getNome());
    }

    private CategoriaDTO mapToDTO(Categoria c) {
        CategoriaDTO dto = new CategoriaDTO();
        dto.setId(c.getId());
        dto.setNome(c.getNome());
        dto.setDescricao(c.getDescricao());
        return dto;
    }
}

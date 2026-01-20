package com.farmacia.system.service;

import com.farmacia.system.dto.MedicamentoDTO;
import com.farmacia.system.entity.Categoria;
import com.farmacia.system.entity.Medicamento;
import com.farmacia.system.exception.BusinessException;
import com.farmacia.system.exception.ResourceNotFoundException;
import com.farmacia.system.repository.MedicamentoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MedicamentoService {

    private final MedicamentoRepository medicamentoRepository;
    private final CategoriaService categoriaService;
    private final AuditService auditService;

    public MedicamentoDTO criar(MedicamentoDTO dto) {
        if (medicamentoRepository.existsByNome(dto.getNome())) {
            throw new BusinessException("Medicamento já existe.");
        }
        if (dto.getValidade() != null && dto.getValidade().isBefore(LocalDate.now())) {
            throw new BusinessException("Validade deve ser futura.");
        }

        Categoria categoria = null;
        if (dto.getCategoriaId() != null) {
            categoria = categoriaService.findEntityById(dto.getCategoriaId());
        }

        Medicamento medicamento = Medicamento.builder()
                .nome(dto.getNome())
                .preco(dto.getPreco())
                .estoque(dto.getEstoque())
                .validade(dto.getValidade())
                .ativo(true)
                .categoria(categoria)
                .descricao(dto.getDescricao())
                .build();

        Medicamento salvo = medicamentoRepository.save(medicamento);
        auditService.log("MEDICAMENTO", "Criado medicamento: " + salvo.getNome());

        return mapToDTO(salvo);
    }

    public MedicamentoDTO atualizar(UUID id, MedicamentoDTO dto) {
        Medicamento medicamento = medicamentoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Medicamento não encontrado."));

        if (!medicamento.getNome().equals(dto.getNome()) && medicamentoRepository.existsByNome(dto.getNome())) {
            throw new BusinessException("Nome de medicamento já existe.");
        }

        Categoria categoria = null;
        if (dto.getCategoriaId() != null) {
            categoria = categoriaService.findEntityById(dto.getCategoriaId());
        }

        medicamento.setNome(dto.getNome());
        medicamento.setPreco(dto.getPreco());
        medicamento.setValidade(dto.getValidade());
        medicamento.setCategoria(categoria);
        medicamento.setEstoque(dto.getEstoque());
        medicamento.setDescricao(dto.getDescricao());

        Medicamento salvo = medicamentoRepository.save(medicamento);
        auditService.log("MEDICAMENTO", "Atualizado medicamento: " + salvo.getNome());
        return mapToDTO(salvo);
    }

    public void atualizarStatus(UUID id, boolean ativo) {
        Medicamento medicamento = medicamentoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Medicamento não encontrado."));
        medicamento.setAtivo(ativo);
        medicamentoRepository.save(medicamento);
        auditService.log("MEDICAMENTO", "Alterado status para " + (ativo ? "ATIVO" : "INATIVO") + " ID: " + id);
    }

    public List<MedicamentoDTO> listar() {
        return medicamentoRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public MedicamentoDTO buscarPorId(UUID id) {
        Medicamento m = medicamentoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Medicamento não encontrado."));
        return mapToDTO(m);
    }

    public void deletar(UUID id) {
        try {
            medicamentoRepository.deleteById(id);
            auditService.log("MEDICAMENTO", "Deletado medicamento ID: " + id);
        } catch (Exception e) {
            // Fallback to soft delete
            atualizarStatus(id, false);
            auditService.log("MEDICAMENTO", "Soft delete (inativado) por vinculo existente ID: " + id);
        }
    }

    // Stock/Alerts
    public void adicionarEstoque(UUID id, int quantidade) {
        if (quantidade <= 0)
            throw new BusinessException("Quantidade deve ser positiva.");
        Medicamento m = medicamentoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Medicamento não encontrado"));
        m.setEstoque(m.getEstoque() + quantidade);
        medicamentoRepository.save(m);
        auditService.log("ESTOQUE", "Entrada de " + quantidade + " no item " + m.getNome());
    }

    public void removerEstoque(UUID id, int quantidade) {
        if (quantidade <= 0)
            throw new BusinessException("Quantidade deve ser positiva.");
        Medicamento m = medicamentoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Medicamento não encontrado"));
        if (m.getEstoque() < quantidade) {
            throw new BusinessException("Estoque insuficiente.");
        }
        m.setEstoque(m.getEstoque() - quantidade);
        medicamentoRepository.save(m);
        auditService.log("ESTOQUE", "Saída de " + quantidade + " no item " + m.getNome());
    }

    public List<MedicamentoDTO> listarAlertasEstoqueBaixo(int limite) {
        return medicamentoRepository.findByEstoqueLessThanAndAtivoTrue(limite)
                .stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    public List<MedicamentoDTO> listarAlertasValidade(int dias) {
        LocalDate limite = LocalDate.now().plusDays(dias);
        return medicamentoRepository.findByValidadeBeforeAndAtivoTrue(limite)
                .stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    private MedicamentoDTO mapToDTO(Medicamento m) {
        MedicamentoDTO dto = new MedicamentoDTO();
        dto.setId(m.getId());
        dto.setNome(m.getNome());
        dto.setPreco(m.getPreco());
        dto.setEstoque(m.getEstoque());
        dto.setValidade(m.getValidade());
        dto.setAtivo(m.isAtivo());
        dto.setDescricao(m.getDescricao());
        if (m.getCategoria() != null) {
            dto.setCategoriaId(m.getCategoria().getId());
            dto.setCategoriaNome(m.getCategoria().getNome());
        }
        return dto;
    }
}

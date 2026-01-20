package com.farmacia.system.service;

import com.farmacia.system.dto.ItemVendaCreateDTO;
import com.farmacia.system.dto.ItemVendaDTO;
import com.farmacia.system.dto.VendaCreateDTO;
import com.farmacia.system.dto.VendaDTO;
import com.farmacia.system.entity.*;
import com.farmacia.system.exception.BusinessException;
import com.farmacia.system.exception.ResourceNotFoundException;
import com.farmacia.system.repository.ClienteRepository;
import com.farmacia.system.repository.MedicamentoRepository;
import com.farmacia.system.repository.UsuarioRepository;
import com.farmacia.system.repository.VendaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VendaService {

    private final VendaRepository vendaRepository;
    private final ClienteRepository clienteRepository;
    private final MedicamentoRepository medicamentoRepository;
    private final UsuarioRepository usuarioRepository;
    private final AuditService auditService;

    @Transactional
    public VendaDTO realizarVenda(VendaCreateDTO dto) {
        Cliente cliente = clienteRepository.findById(dto.getClienteId())
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado."));

        String emailVendedor = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario vendedor = usuarioRepository.findByEmail(emailVendedor)
                .orElseThrow(() -> new ResourceNotFoundException("Vendedor não encontrado."));

        Venda venda = new Venda();
        venda.setCliente(cliente);
        venda.setUsuario(vendedor);

        List<ItemVenda> itens = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;

        for (ItemVendaCreateDTO itemDto : dto.getItens()) {
            Medicamento med = medicamentoRepository.findById(itemDto.getMedicamentoId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Medicamento não encontrado ID: " + itemDto.getMedicamentoId()));

            // Validations
            if (!med.isAtivo())
                throw new BusinessException("Medicamento inativo: " + med.getNome());
            if (med.getValidade() != null && med.getValidade().isBefore(LocalDate.now()))
                throw new BusinessException("Medicamento vencido: " + med.getNome());
            if (med.getEstoque() < itemDto.getQuantidade())
                throw new BusinessException("Estoque insuficiente para: " + med.getNome());

            // Update Stock
            med.setEstoque(med.getEstoque() - itemDto.getQuantidade());
            medicamentoRepository.save(med);

            BigDecimal subtotal = med.getPreco().multiply(BigDecimal.valueOf(itemDto.getQuantidade()));

            ItemVenda item = new ItemVenda();
            item.setVenda(venda);
            item.setMedicamento(med);
            item.setMedicamentoNome(med.getNome());
            item.setQuantidade(itemDto.getQuantidade());
            item.setPrecoUnitario(med.getPreco());
            item.setSubtotal(subtotal);

            itens.add(item);

            total = total.add(subtotal);
        }

        venda.setItens(itens);
        venda.setTotal(total);

        Venda salvo = vendaRepository.save(venda);
        auditService.log("VENDA", "Nova venda realizada ID: " + salvo.getId() + " Total: " + total);

        return mapToDTO(salvo);
    }

    public List<VendaDTO> listar() {
        return vendaRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public VendaDTO buscarPorId(UUID id) {
        return vendaRepository.findById(id).map(this::mapToDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Venda não encontrada."));
    }

    public List<VendaDTO> buscarPorCliente(UUID clienteId) {
        return vendaRepository.findByClienteId(clienteId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    private VendaDTO mapToDTO(Venda v) {
        VendaDTO dto = new VendaDTO();
        dto.setId(v.getId());
        dto.setClienteId(v.getCliente().getId());
        dto.setClienteNome(v.getCliente().getNome());
        dto.setUsuarioNome(v.getUsuario().getNome());
        dto.setStatus(v.getStatus());
        dto.setTotal(v.getTotal());
        dto.setCreatedAt(v.getCreatedAt());
        dto.setItens(v.getItens().stream().map(i -> {
            ItemVendaDTO iv = new ItemVendaDTO();
            iv.setMedicamentoId(i.getMedicamento().getId());
            iv.setMedicamentoNome(i.getMedicamentoNome());
            iv.setQuantidade(i.getQuantidade());
            iv.setPrecoUnitario(i.getPrecoUnitario());
            iv.setSubtotal(i.getSubtotal());
            return iv;
        }).collect(Collectors.toList()));
        return dto;
    }
}

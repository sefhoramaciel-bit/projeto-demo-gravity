package com.farmacia.system.controller;

import com.farmacia.system.dto.EstoqueMovimentoDTO;
import com.farmacia.system.dto.MedicamentoDTO;
import com.farmacia.system.service.MedicamentoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping
@RequiredArgsConstructor
@Tag(name = "Medicamentos e Estoque", description = "Gestão de Produtos e Estoque")
@SecurityRequirement(name = "bearerAuth")
public class MedicamentoController {

    private final MedicamentoService medicamentoService;

    // --- Medicamentos ---

    @PostMapping("/medicamentos")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Criar Medicamento", description = "Apenas ADMIN")
    public ResponseEntity<MedicamentoDTO> criar(@Valid @RequestBody MedicamentoDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(medicamentoService.criar(dto));
    }

    @PutMapping("/medicamentos/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Atualizar Medicamento", description = "Apenas ADMIN")
    public ResponseEntity<MedicamentoDTO> atualizar(@PathVariable UUID id, @Valid @RequestBody MedicamentoDTO dto) {
        return ResponseEntity.ok(medicamentoService.atualizar(id, dto));
    }

    @PatchMapping("/medicamentos/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Ativar/Inativar Medicamento", description = "Apenas ADMIN")
    public ResponseEntity<Void> atualizarStatus(@PathVariable UUID id, @RequestParam boolean ativo) {
        medicamentoService.atualizarStatus(id, ativo);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/medicamentos")
    @Operation(summary = "Listar Medicamentos")
    public ResponseEntity<List<MedicamentoDTO>> listar() {
        return ResponseEntity.ok(medicamentoService.listar());
    }

    @GetMapping("/medicamentos/{id}")
    @Operation(summary = "Buscar Medicamento por ID")
    public ResponseEntity<MedicamentoDTO> buscarPorId(@PathVariable UUID id) {
        return ResponseEntity.ok(medicamentoService.buscarPorId(id));
    }

    @DeleteMapping("/medicamentos/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Deletar Medicamento", description = "Apenas ADMIN")
    public ResponseEntity<Void> deletar(@PathVariable UUID id) {
        medicamentoService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    // --- Estoque ---

    @PostMapping("/estoque/entrada")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Entrada de Estoque", description = "Apenas ADMIN")
    public ResponseEntity<Void> entradaEstoque(@Valid @RequestBody EstoqueMovimentoDTO dto) {
        medicamentoService.adicionarEstoque(dto.getMedicamentoId(), dto.getQuantidade());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/estoque/saida")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Saída de Estoque", description = "Apenas ADMIN")
    public ResponseEntity<Void> saidaEstoque(@Valid @RequestBody EstoqueMovimentoDTO dto) {
        medicamentoService.removerEstoque(dto.getMedicamentoId(), dto.getQuantidade());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/estoque/{medicamentoId}")
    @Operation(summary = "Consultar Estoque de um Medicamento")
    public ResponseEntity<Integer> consultarEstoque(@PathVariable UUID medicamentoId) {
        return ResponseEntity.ok(medicamentoService.buscarPorId(medicamentoId).getEstoque());
    }

    // --- Alertas ---

    @GetMapping("/alertas/estoque-baixo")
    @Operation(summary = "Alerta: Estoque Baixo", description = "Param opcional 'limite', default 10")
    public ResponseEntity<List<MedicamentoDTO>> alertaEstoqueBaixo(@RequestParam(defaultValue = "10") int limite) {
        return ResponseEntity.ok(medicamentoService.listarAlertasEstoqueBaixo(limite));
    }

    @GetMapping("/alertas/validade-proxima")
    @Operation(summary = "Alerta: Validade Próxima", description = "Param opcional 'dias', default 30")
    public ResponseEntity<List<MedicamentoDTO>> alertaValidade(@RequestParam(defaultValue = "30") int dias) {
        return ResponseEntity.ok(medicamentoService.listarAlertasValidade(dias));
    }
}

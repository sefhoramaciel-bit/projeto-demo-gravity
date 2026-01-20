package com.farmacia.system.controller;

import com.farmacia.system.dto.VendaCreateDTO;
import com.farmacia.system.dto.VendaDTO;
import com.farmacia.system.service.VendaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/vendas")
@RequiredArgsConstructor
@Tag(name = "Vendas", description = "Operações de Venda")
@SecurityRequirement(name = "bearerAuth")
public class VendaController {

    private final VendaService vendaService;

    @PostMapping
    @Operation(summary = "Realizar Venda", description = "Registra venda e baixa estoque")
    public ResponseEntity<VendaDTO> realizarVenda(@Valid @RequestBody VendaCreateDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(vendaService.realizarVenda(dto));
    }

    @GetMapping
    @Operation(summary = "Listar Vendas")
    public ResponseEntity<List<VendaDTO>> listar() {
        return ResponseEntity.ok(vendaService.listar());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar Venda por ID")
    public ResponseEntity<VendaDTO> buscarPorId(@PathVariable UUID id) {
        return ResponseEntity.ok(vendaService.buscarPorId(id));
    }

    @GetMapping("/cliente/{clienteId}")
    @Operation(summary = "Buscar Vendas por Cliente")
    public ResponseEntity<List<VendaDTO>> buscarPorCliente(@PathVariable UUID clienteId) {
        return ResponseEntity.ok(vendaService.buscarPorCliente(clienteId));
    }
}

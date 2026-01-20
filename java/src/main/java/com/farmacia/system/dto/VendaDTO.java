package com.farmacia.system.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
public class VendaDTO {
    private UUID id;
    private String clienteNome;
    private UUID clienteId;
    private String usuarioNome; // Nome do usuário que fez a venda
    private String status;
    private BigDecimal total;
    private LocalDateTime createdAt;
    private List<ItemVendaDTO> itens;
}

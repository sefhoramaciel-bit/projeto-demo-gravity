package com.farmacia.system.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.util.UUID;

@Data
public class ItemVendaDTO {
    private String medicamentoNome;
    private UUID medicamentoId;
    private Integer quantidade;
    private BigDecimal precoUnitario;
    private BigDecimal subtotal;
}

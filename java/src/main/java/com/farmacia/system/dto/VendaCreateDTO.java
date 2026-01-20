package com.farmacia.system.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.util.List;
import java.util.UUID;

@Data
public class VendaCreateDTO {
    @NotNull
    private UUID clienteId;

    @NotEmpty
    private List<ItemVendaCreateDTO> itens;
}

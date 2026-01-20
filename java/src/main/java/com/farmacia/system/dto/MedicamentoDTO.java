package com.farmacia.system.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
public class MedicamentoDTO {
    private UUID id;

    @NotBlank
    private String nome;

    @NotNull
    @DecimalMin("0.01")
    private BigDecimal preco;

    @NotNull
    @Min(0)
    private Integer estoque;

    private LocalDate validade; // Pode ser null no banco

    private boolean ativo;

    private UUID categoriaId; // Pode ser null no banco

    private String categoriaNome; // Read-only convenience

    private String descricao;
}

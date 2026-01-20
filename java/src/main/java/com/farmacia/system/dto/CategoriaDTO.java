package com.farmacia.system.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import java.util.UUID;

@Data
public class CategoriaDTO {
    private UUID id;

    @NotBlank
    private String nome;

    private String descricao;
}

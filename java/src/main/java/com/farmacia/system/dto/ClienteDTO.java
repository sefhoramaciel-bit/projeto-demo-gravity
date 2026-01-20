package com.farmacia.system.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import java.time.LocalDate;
import java.util.UUID;

@Data
public class ClienteDTO {
    private UUID id;

    @NotBlank
    @Pattern(regexp = "\\d{11}", message = "CPF deve conter 11 dígitos")
    private String cpf;

    @NotBlank
    private String nome;

    @NotBlank
    @Email
    private String email;

    private LocalDate dataNascimento; // Pode ser null no banco

    private String telefone;

    private String endereco;
}

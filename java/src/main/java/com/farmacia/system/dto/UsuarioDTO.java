package com.farmacia.system.dto;

import com.farmacia.system.entity.Role;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UsuarioDTO {
    private java.util.UUID id;
    private String nome;
    private String email;
    private Role role;
    private String avatarUrl;
    private LocalDateTime createdAt;
}

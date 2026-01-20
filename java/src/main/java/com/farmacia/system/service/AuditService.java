package com.farmacia.system.service;

import com.farmacia.system.entity.Log;
import com.farmacia.system.entity.Usuario;
import com.farmacia.system.repository.LogRepository;
import com.farmacia.system.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuditService {

    private final LogRepository logRepository;
    private final UsuarioRepository usuarioRepository;

    public void log(String tipo, String detalhes) {
        String userEmail = "SYSTEM";
        UUID usuarioId = null;
        String usuarioNome = "SYSTEM";
        try {
            if (SecurityContextHolder.getContext().getAuthentication() != null) {
                userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
                // Tentar obter o ID e nome do usuário
                Usuario usuario = usuarioRepository.findByEmail(userEmail).orElse(null);
                if (usuario != null) {
                    usuarioId = usuario.getId();
                    usuarioNome = usuario.getNome() != null ? usuario.getNome() : usuario.getEmail();
                }
            }
        } catch (Exception e) {
            // ignore
        }
        // Incluir usuário no detalhes
        String detalhesCompleto = userEmail != null ? "Usuario: " + userEmail + ". " : "";
        detalhesCompleto += detalhes;
        // Para operações gerais do sistema, usar UUID aleatório se não houver usuário
        if (usuarioId == null) {
            // Tentar encontrar usuário admin como fallback
            Usuario admin = usuarioRepository.findByEmail("admin@farmacia.com").orElse(null);
            if (admin != null) {
                usuarioId = admin.getId();
                usuarioNome = admin.getNome() != null ? admin.getNome() : admin.getEmail();
                userEmail = admin.getEmail();
            } else {
                usuarioId = UUID.randomUUID();
            }
        }
        logRepository.save(Log.builder()
                .tipoOperacao(tipo != null ? tipo : "SYSTEM")
                .tipoEntidade("SYSTEM") // Tipo de entidade afetada pela operação
                .entidadeId(UUID.randomUUID()) // ID padrão para operações de sistema
                .usuarioId(usuarioId) // ID do usuário que realizou a operação
                .usuarioNome(usuarioNome) // Nome do usuário
                .usuarioEmail(userEmail) // Email do usuário
                .descricao(detalhes != null ? detalhes : "Operação do sistema") // Descrição da operação
                .detalhes(detalhesCompleto)
                .build());
    }
}

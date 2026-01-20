package com.farmacia.system.service;

import com.farmacia.system.dto.UsuarioCreateDTO;
import com.farmacia.system.dto.UsuarioDTO;
import com.farmacia.system.entity.Log;
import com.farmacia.system.entity.Usuario;
import com.farmacia.system.exception.BusinessException;
import com.farmacia.system.exception.ResourceNotFoundException;
import com.farmacia.system.repository.LogRepository;
import com.farmacia.system.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final LogRepository logRepository;

    public UsuarioDTO criarUsuario(UsuarioCreateDTO dto) {
        if (usuarioRepository.existsByEmail(dto.getEmail())) {
            throw new BusinessException("Email já cadastrado.");
        }

        Usuario usuario = Usuario.builder()
                .nome(dto.getNome())
                .email(dto.getEmail())
                .senha(passwordEncoder.encode(dto.getSenha()))
                .role(dto.getRole())
                .avatarUrl(dto.getAvatarUrl())
                .build();

        Usuario salvo = usuarioRepository.save(usuario);

        logAction("USUARIO", "Criado usuário: " + salvo.getEmail());

        return mapToDTO(salvo);
    }

    public UsuarioDTO atualizarUsuario(java.util.UUID id, UsuarioCreateDTO dto) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado."));

        if (!usuario.getEmail().equals(dto.getEmail()) && usuarioRepository.existsByEmail(dto.getEmail())) {
            throw new BusinessException("Email já em uso por outro usuário.");
        }

        usuario.setNome(dto.getNome());
        usuario.setEmail(dto.getEmail());
        if (dto.getSenha() != null && !dto.getSenha().isBlank()) {
            usuario.setSenha(passwordEncoder.encode(dto.getSenha()));
        }
        usuario.setRole(dto.getRole());
        usuario.setAvatarUrl(dto.getAvatarUrl());

        Usuario salvo = usuarioRepository.save(usuario);

        logAction("USUARIO", "Atualizado usuário: " + salvo.getEmail());

        return mapToDTO(salvo);
    }

    public List<UsuarioDTO> listarUsuarios() {
        return usuarioRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public void deletarUsuario(java.util.UUID id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado."));

        usuarioRepository.delete(usuario);
        logAction("USUARIO", "Deletado usuário: " + usuario.getEmail());
    }

    // Seed helper
    public void criarAdminSeNaoExistir() {
        if (usuarioRepository.count() == 0) {
            // .. handled in SeedService or similar, or here.
        }
    }

    private UsuarioDTO mapToDTO(Usuario u) {
        UsuarioDTO dto = new UsuarioDTO();
        dto.setId(u.getId());
        dto.setNome(u.getNome());
        dto.setEmail(u.getEmail());
        dto.setRole(u.getRole());
        dto.setAvatarUrl(u.getAvatarUrl());
        dto.setCreatedAt(u.getCreatedAt());
        return dto;
    }

    private void logAction(String tipo, String detalhes) {
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
        // Se não encontrou usuário, usar admin como fallback
        if (usuarioId == null) {
            Usuario admin = usuarioRepository.findByEmail("admin@farmacia.com").orElse(null);
            if (admin != null) {
                usuarioId = admin.getId();
                usuarioNome = admin.getNome() != null ? admin.getNome() : admin.getEmail();
                userEmail = admin.getEmail();
            } else {
                usuarioId = UUID.randomUUID();
            }
        }
        // Incluir usuário no detalhes
        String detalhesCompleto = userEmail != null ? "Usuario: " + userEmail + ". " : "";
        detalhesCompleto += detalhes;
        logRepository.save(Log.builder()
                .tipoOperacao(tipo != null ? tipo : "SYSTEM")
                .tipoEntidade("USUARIO")
                .entidadeId(usuarioId) // ID da entidade afetada (usuário sendo modificado)
                .usuarioId(usuarioId) // ID do usuário que realizou a operação
                .usuarioNome(usuarioNome) // Nome do usuário
                .usuarioEmail(userEmail) // Email do usuário
                .descricao(detalhes != null ? detalhes : "Operação em usuário") // Descrição da operação
                .detalhes(detalhesCompleto)
                .build());
    }
}

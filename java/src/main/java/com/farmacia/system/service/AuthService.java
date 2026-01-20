package com.farmacia.system.service;

import com.farmacia.system.dto.JwtAuthenticationResponse;
import com.farmacia.system.dto.LoginRequest;
import com.farmacia.system.entity.Log;
import com.farmacia.system.entity.Usuario;
import com.farmacia.system.repository.LogRepository;
import com.farmacia.system.repository.UsuarioRepository;
import com.farmacia.system.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    private final LogRepository logRepository;
    private final UsuarioRepository usuarioRepository;

    public JwtAuthenticationResponse authenticateUser(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getSenha()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.generateToken(authentication);

        // Obter ID do usuário para o log
        Usuario usuario = usuarioRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado após autenticação"));

        // Audit Log - usando tabela 'logs'
        Log log = Log.builder()
                .tipoOperacao("LOGIN")
                .tipoEntidade("USUARIO")
                .entidadeId(usuario.getId()) // ID da entidade afetada (o próprio usuário)
                .usuarioId(usuario.getId()) // ID do usuário que realizou a operação
                .usuarioNome(usuario.getNome() != null ? usuario.getNome() : usuario.getEmail()) // Nome do usuário
                .usuarioEmail(usuario.getEmail()) // Email do usuário
                .descricao("Usuário logou com sucesso")
                .detalhes("Usuario: " + loginRequest.getEmail() + ". Usuário logou com sucesso.")
                .build();
        logRepository.save(log);

        return new JwtAuthenticationResponse(jwt);
    }
}

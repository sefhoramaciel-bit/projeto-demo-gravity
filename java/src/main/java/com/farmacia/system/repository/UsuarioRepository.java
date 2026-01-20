package com.farmacia.system.repository;

import com.farmacia.system.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, java.util.UUID> {
    Optional<Usuario> findByEmail(String email);

    boolean existsByEmail(String email);
}

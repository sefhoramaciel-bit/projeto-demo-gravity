package com.farmacia.system.repository;

import com.farmacia.system.entity.Venda;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface VendaRepository extends JpaRepository<Venda, UUID> {
    List<Venda> findByClienteId(UUID clienteId);
}

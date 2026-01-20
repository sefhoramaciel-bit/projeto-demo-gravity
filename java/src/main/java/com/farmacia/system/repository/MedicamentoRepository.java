package com.farmacia.system.repository;

import com.farmacia.system.entity.Medicamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.UUID;

public interface MedicamentoRepository extends JpaRepository<Medicamento, UUID> {
    boolean existsByNome(String nome);

    List<Medicamento> findByEstoqueLessThanAndAtivoTrue(int estoque);

    // For expiration checks
    @Query("SELECT m FROM Medicamento m WHERE m.validade <= :date AND m.ativo = true")
    List<Medicamento> findByValidadeBeforeAndAtivoTrue(java.time.LocalDate date);

    long countByCategoriaId(UUID categoriaId);
}

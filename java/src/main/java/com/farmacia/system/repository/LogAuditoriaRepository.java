package com.farmacia.system.repository;

import com.farmacia.system.entity.LogAuditoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface LogAuditoriaRepository extends JpaRepository<LogAuditoria, Long> {
    // If using Pageable, default findAll(Pageable) works.
    List<LogAuditoria> findAllByOrderByDataHoraDesc(Pageable pageable);
}

package com.farmacia.system.repository;

import com.farmacia.system.entity.Log;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface LogRepository extends JpaRepository<Log, UUID> {
    List<Log> findAllByOrderByDataHoraDesc(Pageable pageable);
}


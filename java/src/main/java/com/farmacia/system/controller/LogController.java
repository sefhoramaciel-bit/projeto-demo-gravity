package com.farmacia.system.controller;

import com.farmacia.system.entity.Log;
import com.farmacia.system.service.LogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/logs")
@RequiredArgsConstructor
@Tag(name = "Logs e Auditoria", description = "Monitoramento do Sistema")
@SecurityRequirement(name = "bearerAuth")
public class LogController {

    private final LogService logService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Listar Logs Recentes", description = "Últimos 50 logs. Apenas ADMIN.")
    public ResponseEntity<List<Log>> listarLogs() {
        return ResponseEntity.ok(logService.listarUltimosLogs(50));
    }

    @GetMapping("/export")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Exportar Logs CSV", description = "Download de todos os logs. Apenas ADMIN.")
    public void exportarLogs(HttpServletResponse response) throws IOException {
        String csvContent = logService.exportarCsv();

        response.setContentType("text/csv");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"logs.csv\"");
        response.getWriter().write(csvContent);
    }
}

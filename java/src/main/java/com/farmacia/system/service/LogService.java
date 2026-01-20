package com.farmacia.system.service;

import com.farmacia.system.entity.Log;
import com.farmacia.system.repository.LogRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.StringWriter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LogService {

    private final LogRepository logRepository;

    public List<Log> listarUltimosLogs(int limit) {
        return logRepository.findAllByOrderByDataHoraDesc(PageRequest.of(0, limit));
    }

    public List<Log> listarTodos() {
        return logRepository.findAll();
    }

    public String exportarCsv() {
        List<Log> logs = logRepository.findAll();
        StringWriter sw = new StringWriter();
        try (CSVPrinter csvPrinter = new CSVPrinter(sw,
                CSVFormat.DEFAULT.withHeader("ID", "TipoOperacao", "TipoEntidade", "EntidadeId", "Descricao", "UsuarioId", "UsuarioNome", "UsuarioEmail", "DataHora", "Detalhes"))) {
            for (Log log : logs) {
                csvPrinter.printRecord(
                        log.getId(),
                        log.getTipoOperacao(),
                        log.getTipoEntidade(),
                        log.getEntidadeId(),
                        log.getDescricao(),
                        log.getUsuarioId(),
                        log.getUsuarioNome(),
                        log.getUsuarioEmail(),
                        log.getDataHora(),
                        log.getDetalhes());
            }
        } catch (IOException e) {
            throw new RuntimeException("Erro ao gerar CSV", e);
        }
        return sw.toString();
    }
}

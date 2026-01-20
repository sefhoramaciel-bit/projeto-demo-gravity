package com.farmacia.system.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "logs")
public class Log {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(columnDefinition = "uuid")
    private UUID id;

    @Column(name = "tipo_operacao", nullable = false)
    private String tipoOperacao;

    @Column(name = "tipo_entidade", nullable = false)
    private String tipoEntidade;

    @Column(name = "entidade_id", nullable = false)
    private UUID entidadeId;

    @Column(name = "descricao", nullable = false)
    private String descricao;

    @Column(name = "usuario_id", nullable = false)
    private UUID usuarioId;

    @Column(name = "usuario_nome", nullable = false)
    private String usuarioNome;

    @Column(name = "usuario_email", nullable = false)
    private String usuarioEmail;

    @Column(name = "data_hora", nullable = true)
    private LocalDateTime dataHora;

    @Column(columnDefinition = "TEXT", nullable = true)
    private String detalhes;

    @PrePersist
    public void onCreate() {
        if (this.dataHora == null) {
            this.dataHora = LocalDateTime.now();
        }
    }
}


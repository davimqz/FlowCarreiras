package com.flowcarreiras.flowcarreiras_api.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "fila_descoberta_log")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FilaDescobertaLog {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "perfil_id", nullable = false)
    private PerfilArtista perfil;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "obra_id", nullable = false)
    private Obra obra;

    @Column(nullable = false)
    private LocalDateTime dataHora;

    @PrePersist
    private void prePersist() {
        if (dataHora == null) dataHora = LocalDateTime.now();
    }
}

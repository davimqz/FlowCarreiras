package com.flowcarreiras.flowcarreiras_api.model;

import com.flowcarreiras.flowcarreiras_api.model.enums.StatusMentoria;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(
    name = "mentorias",
    indexes = {
        @Index(name = "idx_mentorias_mentor_status", columnList = "mentor_id,status"),
        @Index(name = "idx_mentorias_artista_status", columnList = "artista_id,status")
    }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Mentoria {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "mentor_id", nullable = false)
    private PerfilArtista mentor;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "artista_id", nullable = false)
    private PerfilArtista artista;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private StatusMentoria status = StatusMentoria.ATIVA;

    @Column(nullable = false)
    private LocalDateTime dataCriacao;

    private LocalDateTime dataEncerramento;

    @PrePersist
    private void prePersist() {
        if (status == null) status = StatusMentoria.ATIVA;
        if (dataCriacao == null) dataCriacao = LocalDateTime.now();
    }
}

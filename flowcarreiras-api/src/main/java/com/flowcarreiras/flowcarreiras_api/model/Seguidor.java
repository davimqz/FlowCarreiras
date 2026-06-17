package com.flowcarreiras.flowcarreiras_api.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Relação de "seguir": {@code seguidor} (quem segue) acompanha {@code seguido} (quem é seguido).
 * Ambos são usuários. Cada par (seguidor, seguido) é único.
 */
@Entity
@Table(
    name = "seguidores",
    uniqueConstraints = @UniqueConstraint(
        name = "uk_seguidor_seguido",
        columnNames = {"seguidor_id", "seguido_id"}
    )
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Seguidor {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seguidor_id", nullable = false)
    private Usuario seguidor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seguido_id", nullable = false)
    private Usuario seguido;

    @Column(nullable = false)
    private LocalDateTime dataCriacao;

    @PrePersist
    private void prePersist() {
        if (dataCriacao == null) dataCriacao = LocalDateTime.now();
    }
}

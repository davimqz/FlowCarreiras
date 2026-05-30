package com.flowcarreiras.flowcarreiras_api.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "notificacoes_oportunidades")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificacaoOportunidade {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "perfil_id", nullable = false)
    private PerfilArtista perfil;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "oportunidade_id", nullable = false)
    private Oportunidade oportunidade;

    @Column(nullable = false, length = 200)
    private String titulo;

    @Column(nullable = false, length = 40)
    private String tipo;

    @Column(nullable = false)
    private LocalDate dataEncerramento;

    @Column(nullable = false)
    @Builder.Default
    private Boolean lida = false;

    @Column(nullable = false)
    private LocalDateTime dataCriacao;

    @PrePersist
    private void prePersist() {
        if (dataCriacao == null) dataCriacao = LocalDateTime.now();
        if (lida == null) lida = false;
    }
}

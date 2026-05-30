package com.flowcarreiras.flowcarreiras_api.model;

import com.flowcarreiras.flowcarreiras_api.model.enums.OportunidadeStatus;
import com.flowcarreiras.flowcarreiras_api.model.enums.OportunidadeTipo;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "oportunidades")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Oportunidade {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length = 200)
    private String titulo;

    @Column(length = 2000)
    private String descricao;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 40)
    private OportunidadeTipo tipo;

    @Column(nullable = false, length = 120)
    private String areaArtistica;

    @Column(nullable = false)
    private LocalDate dataEncerramento;

    @Column(nullable = false, length = 500)
    private String linkExterno;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private OportunidadeStatus status = OportunidadeStatus.ATIVA;

    @Column(nullable = false)
    private LocalDateTime dataCriacao;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "oportunidade_tags",
        joinColumns = @JoinColumn(name = "oportunidade_id"),
        inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    @Builder.Default
    private Set<Tag> tags = new HashSet<>();

    @PrePersist
    private void prePersist() {
        if (dataCriacao == null) dataCriacao = LocalDateTime.now();
        if (status == null) status = OportunidadeStatus.ATIVA;
    }
}

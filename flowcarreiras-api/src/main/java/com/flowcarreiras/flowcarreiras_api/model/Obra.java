package com.flowcarreiras.flowcarreiras_api.model;

import com.flowcarreiras.flowcarreiras_api.model.enums.StatusObra;
import com.flowcarreiras.flowcarreiras_api.model.enums.TipoMidia;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "obras")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Obra {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String titulo;

    @Column(length = 2000)
    private String descricao;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoMidia tipoMidia;

    @Column(nullable = false)
    private String urlMidia;

    @Column(nullable = false)
    private LocalDateTime dataPublicacao;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private StatusObra status = StatusObra.RASCUNHO;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "artista_id", nullable = false)
    private PerfilArtista artista;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "obra_tags",
        joinColumns = @JoinColumn(name = "obra_id"),
        inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    @Builder.Default
    private Set<Tag> tags = new HashSet<>();

    @PrePersist
    private void prePersist() {
        if (dataPublicacao == null) dataPublicacao = LocalDateTime.now();
    }
}

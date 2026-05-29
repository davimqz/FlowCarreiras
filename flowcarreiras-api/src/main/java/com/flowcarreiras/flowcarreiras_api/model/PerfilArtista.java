package com.flowcarreiras.flowcarreiras_api.model;

import com.flowcarreiras.flowcarreiras_api.model.enums.StatusEtapaOnboarding;
import com.flowcarreiras.flowcarreiras_api.model.enums.ModalidadeMentoria;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "perfis_artistas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PerfilArtista {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false, unique = true)
    private Usuario usuario;

    @Column(length = 1000)
    private String bio;

    private String fotoPerfil;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(
        name = "perfil_links_externos",
        joinColumns = @JoinColumn(name = "perfil_id")
    )
    @Column(name = "link", length = 300)
    @Builder.Default
    private List<String> linksExternos = new ArrayList<>();

    private String cidade;

    private String areaArtisticaPrincipal;

    @Column(nullable = false)
    @Builder.Default
    private Integer percentualCompletude = 0;

    private LocalDateTime dataEntradaFila;

    @Column(nullable = false)
    @Builder.Default
    private Boolean disponivelParaMentorar = false;

    @Column(nullable = false)
    @Builder.Default
    private Boolean perfilMentorConfigurado = false;

    @Column(nullable = false)
    @Builder.Default
    private Boolean mentoriaGratuita = true;

    @Column(precision = 10, scale = 2)
    private BigDecimal valorHoraMentoria;

    @Column(length = 1000)
    private String descricaoMentoria;

    @Enumerated(EnumType.STRING)
    private ModalidadeMentoria modalidadeMentoria;

    @Column(length = 100)
    private String cidadeMentoria;

    @Column(nullable = false)
    @Builder.Default
    private Boolean onboardingConcluido = false;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private StatusEtapaOnboarding statusEtapaArea = StatusEtapaOnboarding.PENDENTE;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private StatusEtapaOnboarding statusEtapaCidade = StatusEtapaOnboarding.PENDENTE;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private StatusEtapaOnboarding statusEtapaBio = StatusEtapaOnboarding.PENDENTE;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private StatusEtapaOnboarding statusEtapaTags = StatusEtapaOnboarding.PENDENTE;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private StatusEtapaOnboarding statusEtapaFoto = StatusEtapaOnboarding.PENDENTE;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private StatusEtapaOnboarding statusEtapaLinks = StatusEtapaOnboarding.PENDENTE;

    @Column(unique = true, nullable = false)
    private String urlPublica;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "perfil_tags_necessidade",
        joinColumns = @JoinColumn(name = "perfil_id"),
        inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    @Builder.Default
    private Set<Tag> tagsNecessidade = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "perfil_tags_expertise",
        joinColumns = @JoinColumn(name = "perfil_id"),
        inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    @Builder.Default
    private Set<Tag> tagsExpertise = new HashSet<>();

    @OneToMany(mappedBy = "artista", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<Obra> obras = new ArrayList<>();

    public int calcularPercentualCompletude() {
        int pontos = 0;
        if (areaArtisticaPrincipal != null && !areaArtisticaPrincipal.isBlank()) pontos += 30;
        if (tagsNecessidade != null && !tagsNecessidade.isEmpty()) pontos += 25;
        if (cidade != null && !cidade.isBlank()) pontos += 20;
        if (bio != null && !bio.isBlank()) pontos += 10;
        if (fotoPerfil != null && !fotoPerfil.isBlank()) pontos += 10;
        if (linksExternos != null && !linksExternos.isEmpty()) pontos += 5;
        return pontos;
    }
}

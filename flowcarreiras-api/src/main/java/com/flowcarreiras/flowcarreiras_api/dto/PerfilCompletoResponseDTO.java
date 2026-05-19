package com.flowcarreiras.flowcarreiras_api.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Data
@Builder
public class PerfilCompletoResponseDTO {

    // Identidade
    private UUID id;
    private String nome;
    private String urlPublica;

    // Dados do perfil
    private String bio;
    private String fotoPerfil;
    private String cidade;
    private String areaArtisticaPrincipal;
    private Boolean disponivelParaMentorar;
    private Boolean perfilMentorConfigurado;
    private Boolean mentoriaGratuita;
    private BigDecimal valorHoraMentoria;
    private String descricaoMentoria;
    private String modalidadeMentoria;
    private String cidadeMentoria;
    private Integer percentualCompletude;
    private Boolean onboardingConcluido;
    private Integer totalObras;

    // Tags
    private List<TagDTO> tagsExpertise;
    private List<TagDTO> tagsNecessidade;

    // Status de etapas do onboarding (mantido para compatibilidade com Onboarding.jsx)
    private String statusEtapaArea;
    private String statusEtapaCidade;
    private String statusEtapaBio;
    private String statusEtapaTags;
}

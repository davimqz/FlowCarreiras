package com.flowcarreiras.flowcarreiras_api.dto.onboarding;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OnboardingStatusResponseDTO {
    private Boolean onboardingConcluido;
    private String statusEtapaArea;
    private String statusEtapaCidade;
    private String statusEtapaBio;
    private String statusEtapaTags;
    private Integer percentualCompletude;
    private String areaArtisticaPrincipal;
    private String cidade;
    private String bio;
}

package com.flowcarreiras.flowcarreiras_api.dto.onboarding;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class OnboardingAreaRequestDTO {
    @NotBlank(message = "Área artística não pode estar em branco")
    private String areaArtisticaPrincipal;
}

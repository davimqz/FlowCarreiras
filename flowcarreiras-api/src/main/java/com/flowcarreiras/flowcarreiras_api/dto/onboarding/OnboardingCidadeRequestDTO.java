package com.flowcarreiras.flowcarreiras_api.dto.onboarding;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class OnboardingCidadeRequestDTO {
    @NotBlank(message = "Cidade não pode estar em branco")
    private String cidade;
}

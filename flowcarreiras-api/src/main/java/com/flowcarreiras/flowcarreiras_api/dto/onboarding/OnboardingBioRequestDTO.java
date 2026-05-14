package com.flowcarreiras.flowcarreiras_api.dto.onboarding;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class OnboardingBioRequestDTO {
    @NotBlank(message = "Bio não pode estar em branco")
    private String bio;
}

package com.flowcarreiras.flowcarreiras_api.dto.onboarding;

import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class OnboardingLinksRequestDTO {

    @Size(max = 5, message = "Limite de 5 links")
    private List<@Size(max = 300, message = "Link deve ter no maximo 300 caracteres") String> linksExternos;
}

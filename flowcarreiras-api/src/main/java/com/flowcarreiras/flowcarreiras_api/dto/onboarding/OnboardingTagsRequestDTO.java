package com.flowcarreiras.flowcarreiras_api.dto.onboarding;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class OnboardingTagsRequestDTO {
    @NotEmpty(message = "Selecione ao menos uma tag")
    private List<UUID> tagNecessidadeIds;
}

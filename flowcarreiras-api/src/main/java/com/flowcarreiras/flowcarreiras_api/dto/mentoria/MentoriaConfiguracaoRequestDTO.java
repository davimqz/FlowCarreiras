package com.flowcarreiras.flowcarreiras_api.dto.mentoria;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Data
public class MentoriaConfiguracaoRequestDTO {

    @NotNull(message = "Informe se a mentoria e gratuita")
    private Boolean mentoriaGratuita;

    @Positive(message = "Valor por hora deve ser maior que zero")
    private BigDecimal valorHoraMentoria;

    @NotBlank(message = "Descricao da mentoria e obrigatoria")
    @Size(max = 1000, message = "Descricao da mentoria deve ter no maximo 1000 caracteres")
    private String descricaoMentoria;

    @NotBlank(message = "Modalidade da mentoria e obrigatoria")
    private String modalidadeMentoria;

    @Size(max = 100, message = "Cidade da mentoria deve ter no maximo 100 caracteres")
    private String cidadeMentoria;

    private Boolean disponivelParaMentorar;

    private List<UUID> tagsExpertiseIds;
}

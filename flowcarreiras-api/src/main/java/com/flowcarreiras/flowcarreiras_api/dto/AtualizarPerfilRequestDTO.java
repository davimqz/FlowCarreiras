package com.flowcarreiras.flowcarreiras_api.dto;

import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Data
public class AtualizarPerfilRequestDTO {

    @Size(max = 1000, message = "Bio deve ter no máximo 1000 caracteres")
    private String bio;

    @Size(max = 100, message = "Cidade deve ter no máximo 100 caracteres")
    private String cidade;

    @Size(max = 100, message = "Área artística deve ter no máximo 100 caracteres")
    private String areaArtisticaPrincipal;

    private Boolean disponivelParaMentorar;

    private Boolean perfilMentorConfigurado;

    private Boolean mentoriaGratuita;

    private BigDecimal valorHoraMentoria;

    @Size(max = 1000, message = "DescriÃ§Ã£o da mentoria deve ter no mÃ¡ximo 1000 caracteres")
    private String descricaoMentoria;

    private String modalidadeMentoria;

    @Size(max = 100, message = "Cidade da mentoria deve ter no mÃ¡ximo 100 caracteres")
    private String cidadeMentoria;

    private List<UUID> tagsExpertiseIds;

    private List<UUID> tagsNecessidadeIds;

    private List<String> linksExternos;
}

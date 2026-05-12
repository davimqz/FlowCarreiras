package com.flowcarreiras.flowcarreiras_api.dto;

import com.flowcarreiras.flowcarreiras_api.model.enums.StatusObra;
import com.flowcarreiras.flowcarreiras_api.model.enums.TipoMidia;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class ObraRequestDTO {

    @NotBlank(message = "Título é obrigatório")
    @Size(max = 200, message = "Título deve ter no máximo 200 caracteres")
    private String titulo;

    @Size(max = 2000, message = "Descrição deve ter no máximo 2000 caracteres")
    private String descricao;

    @NotNull(message = "Tipo de mídia é obrigatório")
    private TipoMidia tipoMidia;

    // Obrigatório apenas para EMBED; para upload de arquivo, será preenchido pelo backend
    private String urlMidia;

    @NotNull(message = "Status é obrigatório")
    private StatusObra status;

    @NotEmpty(message = "Adicione ao menos uma tag antes de publicar")
    @Size(max = 10, message = "Máximo de 10 tags por obra")
    private List<UUID> tagIds;
}

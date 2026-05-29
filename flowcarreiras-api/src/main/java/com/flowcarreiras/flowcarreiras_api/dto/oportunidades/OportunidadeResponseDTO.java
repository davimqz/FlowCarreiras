package com.flowcarreiras.flowcarreiras_api.dto.oportunidades;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class OportunidadeResponseDTO {
    private String id;
    private String titulo;
    private String descricao;
    private String tipo;
    private String data;
    private String localidade;
    private String link;
    private String areaArtistica;
    private String fonte;
    private List<String> tags;
}

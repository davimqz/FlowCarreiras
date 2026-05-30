package com.flowcarreiras.flowcarreiras_api.dto.oportunidades;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
public class OportunidadeResponseDTO {
    private String id;
    private String titulo;
    private String descricao;
    private String tipo;
    private LocalDate dataEncerramento;
    private String linkExterno;
    private String areaArtistica;
    private List<String> tags;
}

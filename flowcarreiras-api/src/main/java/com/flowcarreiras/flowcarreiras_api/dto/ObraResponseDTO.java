package com.flowcarreiras.flowcarreiras_api.dto;

import com.flowcarreiras.flowcarreiras_api.model.Obra;
import com.flowcarreiras.flowcarreiras_api.model.enums.StatusObra;
import com.flowcarreiras.flowcarreiras_api.model.enums.TipoMidia;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Data
@Builder
public class ObraResponseDTO {

    private UUID id;
    private String titulo;
    private String descricao;
    private TipoMidia tipoMidia;
    private String urlMidia;
    private LocalDateTime dataPublicacao;
    private StatusObra status;
    private List<TagDTO> tags;
    private UUID artistaId;
    private String artistaNome;
    private String artistaUrlPublica;

    public static ObraResponseDTO from(Obra obra) {
        return ObraResponseDTO.builder()
                .id(obra.getId())
                .titulo(obra.getTitulo())
                .descricao(obra.getDescricao())
                .tipoMidia(obra.getTipoMidia())
                .urlMidia(obra.getUrlMidia())
                .dataPublicacao(obra.getDataPublicacao())
                .status(obra.getStatus())
                .tags(obra.getTags().stream().map(TagDTO::from).collect(Collectors.toList()))
                .artistaId(obra.getArtista().getId())
                .artistaNome(obra.getArtista().getUsuario().getNome())
                .artistaUrlPublica(obra.getArtista().getUrlPublica())
                .build();
    }
}

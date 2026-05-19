package com.flowcarreiras.flowcarreiras_api.dto.mentoria;

import com.flowcarreiras.flowcarreiras_api.dto.TagDTO;
import com.flowcarreiras.flowcarreiras_api.model.PerfilArtista;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class MentoriaConfiguracaoResponseDTO {

    private Boolean perfilMentorConfigurado;
    private Boolean disponivelParaMentorar;
    private Boolean mentoriaGratuita;
    private BigDecimal valorHoraMentoria;
    private String descricaoMentoria;
    private String modalidadeMentoria;
    private String cidadeMentoria;
    private List<TagDTO> tagsExpertise;

    public static MentoriaConfiguracaoResponseDTO from(PerfilArtista perfil) {
        return MentoriaConfiguracaoResponseDTO.builder()
                .perfilMentorConfigurado(perfil.getPerfilMentorConfigurado())
                .disponivelParaMentorar(perfil.getDisponivelParaMentorar())
                .mentoriaGratuita(perfil.getMentoriaGratuita())
                .valorHoraMentoria(perfil.getValorHoraMentoria())
                .descricaoMentoria(perfil.getDescricaoMentoria())
                .modalidadeMentoria(perfil.getModalidadeMentoria() != null ? perfil.getModalidadeMentoria().name() : null)
                .cidadeMentoria(perfil.getCidadeMentoria())
                .tagsExpertise(perfil.getTagsExpertise().stream().map(TagDTO::from).toList())
                .build();
    }
}

package com.flowcarreiras.flowcarreiras_api.dto.mentoria;

import com.flowcarreiras.flowcarreiras_api.dto.TagDTO;
import com.flowcarreiras.flowcarreiras_api.model.PerfilArtista;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Data
@Builder
public class MentorResumoDTO {

    private UUID id;
    private String nome;
    private String bio;
    private String fotoPerfil;
    private String cidade;
    private String areaArtisticaPrincipal;
    private String urlPublica;
    private String descricaoMentoria;
    private Boolean mentoriaGratuita;
    private BigDecimal valorHoraMentoria;
    private String modalidadeMentoria;
    private String cidadeMentoria;
    private List<TagDTO> tagsExpertise;

    public static MentorResumoDTO from(PerfilArtista perfil) {
        return MentorResumoDTO.builder()
                .id(perfil.getId())
                .nome(perfil.getUsuario().getNome())
                .bio(perfil.getBio())
                .fotoPerfil(perfil.getFotoPerfil())
                .cidade(perfil.getCidade())
                .areaArtisticaPrincipal(perfil.getAreaArtisticaPrincipal())
                .urlPublica(perfil.getUrlPublica())
                .descricaoMentoria(perfil.getDescricaoMentoria())
                .mentoriaGratuita(perfil.getMentoriaGratuita())
                .valorHoraMentoria(perfil.getValorHoraMentoria())
                .modalidadeMentoria(perfil.getModalidadeMentoria() != null ? perfil.getModalidadeMentoria().name() : null)
                .cidadeMentoria(perfil.getCidadeMentoria())
                .tagsExpertise(perfil.getTagsExpertise().stream().map(TagDTO::from).toList())
                .build();
    }
}

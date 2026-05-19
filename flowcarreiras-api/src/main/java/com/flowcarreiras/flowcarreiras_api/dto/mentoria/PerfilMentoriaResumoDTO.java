package com.flowcarreiras.flowcarreiras_api.dto.mentoria;

import com.flowcarreiras.flowcarreiras_api.dto.TagDTO;
import com.flowcarreiras.flowcarreiras_api.model.PerfilArtista;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@Builder
public class PerfilMentoriaResumoDTO {

    private UUID id;
    private String nome;
    private String bio;
    private String fotoPerfil;
    private String cidade;
    private String areaArtisticaPrincipal;
    private String urlPublica;
    private Integer totalObras;
    private List<TagDTO> tagsExpertise;
    private List<TagDTO> tagsNecessidade;

    public static PerfilMentoriaResumoDTO from(PerfilArtista perfil) {
        return PerfilMentoriaResumoDTO.builder()
                .id(perfil.getId())
                .nome(perfil.getUsuario().getNome())
                .bio(perfil.getBio())
                .fotoPerfil(perfil.getFotoPerfil())
                .cidade(perfil.getCidade())
                .areaArtisticaPrincipal(perfil.getAreaArtisticaPrincipal())
                .urlPublica(perfil.getUrlPublica())
                .totalObras(perfil.getObras().size())
                .tagsExpertise(perfil.getTagsExpertise().stream().map(TagDTO::from).toList())
                .tagsNecessidade(perfil.getTagsNecessidade().stream().map(TagDTO::from).toList())
                .build();
    }
}

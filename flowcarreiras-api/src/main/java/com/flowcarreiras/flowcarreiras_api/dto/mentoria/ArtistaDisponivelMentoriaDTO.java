package com.flowcarreiras.flowcarreiras_api.dto.mentoria;

import com.flowcarreiras.flowcarreiras_api.dto.TagDTO;
import com.flowcarreiras.flowcarreiras_api.model.PerfilArtista;
import com.flowcarreiras.flowcarreiras_api.model.Tag;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Data
@Builder
public class ArtistaDisponivelMentoriaDTO {

    private UUID id;
    private String nome;
    private String bio;
    private String fotoPerfil;
    private String cidade;
    private String areaArtisticaPrincipal;
    private String urlPublica;
    private Integer totalObras;
    private List<TagDTO> tagsNecessidade;
    private List<TagDTO> tagsCompativeis;
    private Integer quantidadeTagsCompativeis;
    private Integer percentualCompatibilidade;

    public static ArtistaDisponivelMentoriaDTO from(PerfilArtista artista, PerfilArtista mentor) {
        Set<UUID> expertiseMentorIds = mentor.getTagsExpertise()
                .stream()
                .map(Tag::getId)
                .collect(Collectors.toSet());

        List<TagDTO> tagsCompativeis = artista.getTagsNecessidade()
                .stream()
                .filter(tag -> expertiseMentorIds.contains(tag.getId()))
                .map(TagDTO::from)
                .toList();

        int totalNecessidades = artista.getTagsNecessidade().size();
        int quantidadeCompativel = tagsCompativeis.size();
        int percentual = totalNecessidades == 0
                ? 0
                : Math.round((quantidadeCompativel * 100f) / totalNecessidades);

        return ArtistaDisponivelMentoriaDTO.builder()
                .id(artista.getId())
                .nome(artista.getUsuario().getNome())
                .bio(artista.getBio())
                .fotoPerfil(artista.getFotoPerfil())
                .cidade(artista.getCidade())
                .areaArtisticaPrincipal(artista.getAreaArtisticaPrincipal())
                .urlPublica(artista.getUrlPublica())
                .totalObras(artista.getObras().size())
                .tagsNecessidade(artista.getTagsNecessidade().stream().map(TagDTO::from).toList())
                .tagsCompativeis(tagsCompativeis)
                .quantidadeTagsCompativeis(quantidadeCompativel)
                .percentualCompatibilidade(percentual)
                .build();
    }
}

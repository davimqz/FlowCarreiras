package com.flowcarreiras.flowcarreiras_api.dto;

import com.flowcarreiras.flowcarreiras_api.model.PerfilArtista;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@Builder
public class PortfolioPublicoDTO {

    private ArtistaResumoDTO artista;
    private List<ObraResponseDTO> obras;

    @Data
    @Builder
    public static class ArtistaResumoDTO {
        private UUID id;
        private String nome;
        private String bio;
        private String cidade;
        private String areaArtisticaPrincipal;
        private String fotoPerfil;
        private String urlPublica;

        public static ArtistaResumoDTO from(PerfilArtista perfil) {
            return ArtistaResumoDTO.builder()
                    .id(perfil.getId())
                    .nome(perfil.getUsuario().getNome())
                    .bio(perfil.getBio())
                    .cidade(perfil.getCidade())
                    .areaArtisticaPrincipal(perfil.getAreaArtisticaPrincipal())
                    .fotoPerfil(perfil.getFotoPerfil())
                    .urlPublica(perfil.getUrlPublica())
                    .build();
        }
    }
}

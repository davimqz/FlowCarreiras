package com.flowcarreiras.flowcarreiras_api.dto.mentoria;

import com.flowcarreiras.flowcarreiras_api.model.Mentoria;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class MentoriaResponseDTO {

    private UUID id;
    private String status;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataEncerramento;
    private PerfilMentoriaResumoDTO mentor;
    private PerfilMentoriaResumoDTO artista;

    public static MentoriaResponseDTO from(Mentoria mentoria) {
        return MentoriaResponseDTO.builder()
                .id(mentoria.getId())
                .status(mentoria.getStatus().name())
                .dataCriacao(mentoria.getDataCriacao())
                .dataEncerramento(mentoria.getDataEncerramento())
                .mentor(PerfilMentoriaResumoDTO.from(mentoria.getMentor()))
                .artista(PerfilMentoriaResumoDTO.from(mentoria.getArtista()))
                .build();
    }
}

package com.flowcarreiras.flowcarreiras_api.dto.mentoria;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class MinhasMentoriasResponseDTO {

    private List<MentoriaResponseDTO> comoMentor;
    private List<MentoriaResponseDTO> comoArtista;
}

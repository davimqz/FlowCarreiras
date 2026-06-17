package com.flowcarreiras.flowcarreiras_api.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SeguidorStatusDTO {

    private long totalSeguidores;     // quantos seguem o usuário-alvo
    private long totalSeguindo;       // quantos o usuário-alvo segue
    private boolean seguindoPeloUsuario; // o usuário logado segue o alvo?
}

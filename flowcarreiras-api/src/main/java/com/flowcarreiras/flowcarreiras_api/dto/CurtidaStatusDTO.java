package com.flowcarreiras.flowcarreiras_api.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CurtidaStatusDTO {

    private long total;
    private boolean curtidoPeloUsuario;
}

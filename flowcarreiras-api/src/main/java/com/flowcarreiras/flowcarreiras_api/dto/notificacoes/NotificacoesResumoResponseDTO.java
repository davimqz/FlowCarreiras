package com.flowcarreiras.flowcarreiras_api.dto.notificacoes;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class NotificacoesResumoResponseDTO {

    private long totalNaoLidas;
    private List<NotificacaoOportunidadeResponseDTO> notificacoes;
}

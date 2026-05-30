package com.flowcarreiras.flowcarreiras_api.dto.notificacoes;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class NotificacaoOportunidadeResponseDTO {

    private UUID id;
    private UUID oportunidadeId;
    private String titulo;
    private String tipo;
    private LocalDate dataEncerramento;
    private Boolean lida;
    private LocalDateTime dataCriacao;
}

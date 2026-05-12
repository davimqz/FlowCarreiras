package com.flowcarreiras.flowcarreiras_api.dto.auth;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class AuthResponseDTO {

    private String token;
    private String tipo;
    private UUID usuarioId;
    private String nome;
    private String email;
    private UUID perfilArtistaId;
    private String urlPublica;
}

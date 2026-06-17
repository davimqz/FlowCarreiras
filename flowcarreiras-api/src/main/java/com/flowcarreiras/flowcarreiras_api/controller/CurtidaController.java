package com.flowcarreiras.flowcarreiras_api.controller;

import com.flowcarreiras.flowcarreiras_api.dto.CurtidaStatusDTO;
import com.flowcarreiras.flowcarreiras_api.service.CurtidaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/obras/{obraId}/curtidas")
@RequiredArgsConstructor
public class CurtidaController {

    private final CurtidaService curtidaService;

    // Público — total de curtidas; informa se o usuário logado (se houver) já curtiu
    @GetMapping
    ResponseEntity<CurtidaStatusDTO> status(
            @PathVariable UUID obraId,
            @AuthenticationPrincipal UserDetails userDetails) {
        String email = userDetails != null ? userDetails.getUsername() : null;
        return ResponseEntity.ok(curtidaService.obterStatus(obraId, email));
    }

    // Autenticado — curtir (idempotente)
    @PostMapping
    ResponseEntity<CurtidaStatusDTO> curtir(
            @PathVariable UUID obraId,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(curtidaService.curtir(obraId, userDetails.getUsername()));
    }

    // Autenticado — remover a curtida (idempotente)
    @DeleteMapping
    ResponseEntity<CurtidaStatusDTO> descurtir(
            @PathVariable UUID obraId,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(curtidaService.descurtir(obraId, userDetails.getUsername()));
    }
}

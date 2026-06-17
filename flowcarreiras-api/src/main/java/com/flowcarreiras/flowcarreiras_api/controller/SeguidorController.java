package com.flowcarreiras.flowcarreiras_api.controller;

import com.flowcarreiras.flowcarreiras_api.dto.SeguidorStatusDTO;
import com.flowcarreiras.flowcarreiras_api.service.SeguidorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/usuarios/{usuarioId}/seguidores")
@RequiredArgsConstructor
public class SeguidorController {

    private final SeguidorService seguidorService;

    // Público — contagens de seguidores/seguindo; informa se o usuário logado já segue
    @GetMapping
    ResponseEntity<SeguidorStatusDTO> status(
            @PathVariable UUID usuarioId,
            @AuthenticationPrincipal UserDetails userDetails) {
        String email = userDetails != null ? userDetails.getUsername() : null;
        return ResponseEntity.ok(seguidorService.obterStatus(usuarioId, email));
    }

    // Autenticado — passar a seguir (idempotente)
    @PostMapping
    ResponseEntity<SeguidorStatusDTO> seguir(
            @PathVariable UUID usuarioId,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(seguidorService.seguir(usuarioId, userDetails.getUsername()));
    }

    // Autenticado — deixar de seguir (idempotente)
    @DeleteMapping
    ResponseEntity<SeguidorStatusDTO> deixarDeSeguir(
            @PathVariable UUID usuarioId,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(seguidorService.deixarDeSeguir(usuarioId, userDetails.getUsername()));
    }
}

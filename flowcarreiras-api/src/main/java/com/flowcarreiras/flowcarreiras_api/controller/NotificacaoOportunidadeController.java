package com.flowcarreiras.flowcarreiras_api.controller;

import com.flowcarreiras.flowcarreiras_api.dto.notificacoes.NotificacoesResumoResponseDTO;
import com.flowcarreiras.flowcarreiras_api.service.NotificacaoOportunidadeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/notificacoes/oportunidades")
@RequiredArgsConstructor
public class NotificacaoOportunidadeController {

    private final NotificacaoOportunidadeService notificacaoService;

    @GetMapping
    ResponseEntity<NotificacoesResumoResponseDTO> listar(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(defaultValue = "10") int limit) {
        return ResponseEntity.ok(notificacaoService.listarResumo(userDetails.getUsername(), limit));
    }

    @PatchMapping("/{id}/lida")
    ResponseEntity<Void> marcarLida(
            @PathVariable UUID id,
            @AuthenticationPrincipal UserDetails userDetails) {
        notificacaoService.marcarComoLida(id, userDetails.getUsername());
        return ResponseEntity.noContent().build();
    }
}

package com.flowcarreiras.flowcarreiras_api.controller;

import com.flowcarreiras.flowcarreiras_api.dto.ObraRequestDTO;
import com.flowcarreiras.flowcarreiras_api.dto.ObraResponseDTO;
import com.flowcarreiras.flowcarreiras_api.service.ObraService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.UUID;

@RestController
@RequestMapping("/api/obras")
@RequiredArgsConstructor
public class ObraController {

    private final ObraService obraService;

    // Público — apenas obras publicadas (sem login)
    @GetMapping("/artista/{artistaId}")
    ResponseEntity<List<ObraResponseDTO>> listarPublicadasPorArtista(@PathVariable UUID artistaId) {
        return ResponseEntity.ok(obraService.listarObrasPorArtista(artistaId, true));
    }

    // Autenticado — todas as obras do usuário logado (inclui rascunhos)
    @GetMapping("/minhas")
    ResponseEntity<List<ObraResponseDTO>> listarMinhasObras(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(obraService.listarObrasPorEmail(userDetails.getUsername()));
    }

    // Público — detalhe de uma obra
    @GetMapping("/{id}")
    ResponseEntity<ObraResponseDTO> buscarPorId(@PathVariable UUID id) {
        return ResponseEntity.ok(obraService.buscarPorId(id));
    }

    // Público — exploração com filtros
    @GetMapping("/explorar")
    ResponseEntity<List<ObraResponseDTO>> explorar(
            @RequestParam(required = false) String tags,
            @RequestParam(required = false) String area,
            @RequestParam(required = false) String formatos,
            @RequestParam(required = false) String recencia
    ) {
        return ResponseEntity.ok(obraService.explorar(
                area,
                splitLower(tags),
                splitFormatos(formatos),
                parseRecencia(recencia)
        ));
    }

    // Autenticado — upload com multipart/form-data
    // O campo "dados" deve ser enviado como JSON part (Content-Type: application/json)
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ResponseEntity<ObraResponseDTO> criarObra(
            @RequestPart("dados") @Valid ObraRequestDTO dados,
            @RequestPart(value = "file", required = false) MultipartFile file,
            @AuthenticationPrincipal UserDetails userDetails) {

        ObraResponseDTO response = obraService.criarObra(dados, file, userDetails.getUsername());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // Autenticado — edição
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ResponseEntity<ObraResponseDTO> editarObra(
            @PathVariable UUID id,
            @RequestPart("dados") @Valid ObraRequestDTO dados,
            @RequestPart(value = "file", required = false) MultipartFile file,
            @AuthenticationPrincipal UserDetails userDetails) {

        return ResponseEntity.ok(obraService.editarObra(id, dados, file, userDetails.getUsername()));
    }

    // Autenticado — remoção permanente
    @DeleteMapping("/{id}")
    ResponseEntity<Void> removerObra(
            @PathVariable UUID id,
            @AuthenticationPrincipal UserDetails userDetails) {

        obraService.removerObra(id, userDetails.getUsername());
        return ResponseEntity.noContent().build();
    }

    private Set<String> splitLower(String raw) {
        if (raw == null || raw.isBlank()) return Set.of();
        return java.util.Arrays.stream(raw.split(","))
                .map(String::trim)
                .filter(s -> !s.isBlank())
                .map(s -> s.toLowerCase(Locale.ROOT))
                .collect(Collectors.toSet());
    }

    private Set<com.flowcarreiras.flowcarreiras_api.model.enums.TipoMidia> splitFormatos(String raw) {
        if (raw == null || raw.isBlank()) return Set.of();
        return java.util.Arrays.stream(raw.split(","))
                .map(String::trim)
                .filter(s -> !s.isBlank())
                .map(s -> s.toUpperCase(Locale.ROOT))
                .map(this::parseFormato)
                .filter(java.util.Objects::nonNull)
                .collect(Collectors.toSet());
    }

    private com.flowcarreiras.flowcarreiras_api.model.enums.TipoMidia parseFormato(String raw) {
        try {
            return com.flowcarreiras.flowcarreiras_api.model.enums.TipoMidia.valueOf(raw);
        } catch (IllegalArgumentException ex) {
            return null;
        }
    }

    private ObraService.PeriodoRecencia parseRecencia(String raw) {
        if (raw == null || raw.isBlank()) return ObraService.PeriodoRecencia.SEM_FILTRO;
        String valor = raw.trim().toLowerCase(Locale.ROOT);
        return switch (valor) {
            case "semana" -> ObraService.PeriodoRecencia.ESTA_SEMANA;
            case "mes" -> ObraService.PeriodoRecencia.ESTE_MES;
            default -> ObraService.PeriodoRecencia.SEM_FILTRO;
        };
    }
}

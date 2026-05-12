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
}

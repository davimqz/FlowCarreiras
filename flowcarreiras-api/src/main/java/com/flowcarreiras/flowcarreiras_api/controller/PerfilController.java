package com.flowcarreiras.flowcarreiras_api.controller;

import com.flowcarreiras.flowcarreiras_api.dto.AtualizarPerfilRequestDTO;
import com.flowcarreiras.flowcarreiras_api.dto.PerfilCompletoResponseDTO;
import com.flowcarreiras.flowcarreiras_api.dto.onboarding.*;
import com.flowcarreiras.flowcarreiras_api.service.PerfilService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/perfil")
@RequiredArgsConstructor
public class PerfilController {

    private final PerfilService perfilService;

    @GetMapping("/me")
    ResponseEntity<PerfilCompletoResponseDTO> obterPerfil(Authentication auth) {
        return ResponseEntity.ok(perfilService.obterPerfilCompleto(auth.getName()));
    }

    @PatchMapping
    ResponseEntity<PerfilCompletoResponseDTO> atualizar(
            @RequestBody @Valid AtualizarPerfilRequestDTO dto, Authentication auth) {
        return ResponseEntity.ok(perfilService.atualizarPerfil(auth.getName(), dto));
    }

    @PatchMapping(value = "/foto", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ResponseEntity<PerfilCompletoResponseDTO> atualizarFoto(
            @RequestPart("foto") MultipartFile foto, Authentication auth) {
        return ResponseEntity.ok(perfilService.atualizarFotoPerfil(auth.getName(), foto));
    }

    // --- Endpoints de onboarding ---

    @PatchMapping("/onboarding/area-artistica")
    ResponseEntity<OnboardingStatusResponseDTO> salvarArea(
            @RequestBody @Valid OnboardingAreaRequestDTO dto, Authentication auth) {
        return ResponseEntity.ok(perfilService.salvarEtapaArea(auth.getName(), dto.getAreaArtisticaPrincipal()));
    }

    @PatchMapping("/onboarding/cidade")
    ResponseEntity<OnboardingStatusResponseDTO> salvarCidade(
            @RequestBody @Valid OnboardingCidadeRequestDTO dto, Authentication auth) {
        return ResponseEntity.ok(perfilService.salvarEtapaCidade(auth.getName(), dto.getCidade()));
    }

    @PatchMapping("/onboarding/bio")
    ResponseEntity<OnboardingStatusResponseDTO> salvarBio(
            @RequestBody @Valid OnboardingBioRequestDTO dto, Authentication auth) {
        return ResponseEntity.ok(perfilService.salvarEtapaBio(auth.getName(), dto.getBio()));
    }

    @PatchMapping("/onboarding/tags")
    ResponseEntity<OnboardingStatusResponseDTO> salvarTags(
            @RequestBody @Valid OnboardingTagsRequestDTO dto, Authentication auth) {
        return ResponseEntity.ok(perfilService.salvarEtapaTags(auth.getName(), dto.getTagNecessidadeIds()));
    }

    @PatchMapping(value = "/onboarding/foto", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ResponseEntity<OnboardingStatusResponseDTO> salvarFoto(
            @RequestPart("foto") MultipartFile foto, Authentication auth) {
        return ResponseEntity.ok(perfilService.salvarEtapaFoto(auth.getName(), foto));
    }

    @PatchMapping("/onboarding/links")
    ResponseEntity<OnboardingStatusResponseDTO> salvarLinks(
            @RequestBody @Valid OnboardingLinksRequestDTO dto, Authentication auth) {
        return ResponseEntity.ok(perfilService.salvarEtapaLinks(auth.getName(), dto.getLinksExternos()));
    }

    @PatchMapping("/onboarding/pular/{etapa}")
    ResponseEntity<OnboardingStatusResponseDTO> pularEtapa(
            @PathVariable String etapa, Authentication auth) {
        return ResponseEntity.ok(perfilService.pularEtapa(auth.getName(), etapa));
    }

    @PostMapping("/onboarding/concluir")
    ResponseEntity<OnboardingStatusResponseDTO> concluir(Authentication auth) {
        return ResponseEntity.ok(perfilService.concluirOnboarding(auth.getName()));
    }
}

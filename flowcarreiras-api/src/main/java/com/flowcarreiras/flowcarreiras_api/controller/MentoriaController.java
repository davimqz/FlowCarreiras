package com.flowcarreiras.flowcarreiras_api.controller;

import com.flowcarreiras.flowcarreiras_api.dto.mentoria.ArtistaDisponivelMentoriaDTO;
import com.flowcarreiras.flowcarreiras_api.dto.mentoria.MentoriaConfiguracaoRequestDTO;
import com.flowcarreiras.flowcarreiras_api.dto.mentoria.MentoriaConfiguracaoResponseDTO;
import com.flowcarreiras.flowcarreiras_api.dto.mentoria.MentoriaResponseDTO;
import com.flowcarreiras.flowcarreiras_api.dto.mentoria.MentorResumoDTO;
import com.flowcarreiras.flowcarreiras_api.dto.mentoria.MinhasMentoriasResponseDTO;
import com.flowcarreiras.flowcarreiras_api.service.MentoriaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/mentorias")
@RequiredArgsConstructor
public class MentoriaController {

    private final MentoriaService mentoriaService;

    @GetMapping("/configuracao")
    ResponseEntity<MentoriaConfiguracaoResponseDTO> obterConfiguracao(Authentication auth) {
        return ResponseEntity.ok(mentoriaService.obterConfiguracao(auth.getName()));
    }

    @PutMapping("/configuracao")
    ResponseEntity<MentoriaConfiguracaoResponseDTO> salvarConfiguracao(
            @RequestBody @Valid MentoriaConfiguracaoRequestDTO dto,
            Authentication auth) {
        return ResponseEntity.ok(mentoriaService.salvarConfiguracao(auth.getName(), dto));
    }

    @PatchMapping("/ativar")
    ResponseEntity<MentoriaConfiguracaoResponseDTO> ativar(Authentication auth) {
        return ResponseEntity.ok(mentoriaService.ativar(auth.getName()));
    }

    @PatchMapping("/pausar")
    ResponseEntity<MentoriaConfiguracaoResponseDTO> pausar(Authentication auth) {
        return ResponseEntity.ok(mentoriaService.pausar(auth.getName()));
    }

    @GetMapping("/mentores")
    ResponseEntity<List<MentorResumoDTO>> listarMentoresAtivos(
            @RequestParam(required = false) String cidade,
            @RequestParam(required = false) String modalidade,
            @RequestParam(required = false) Boolean gratuita,
            @RequestParam(required = false) String tag,
            @RequestParam(required = false) String area) {
        return ResponseEntity.ok(mentoriaService.listarMentoresAtivos(cidade, modalidade, gratuita, tag, area));
    }

    @GetMapping("/artistas-disponiveis")
    ResponseEntity<List<ArtistaDisponivelMentoriaDTO>> listarArtistasDisponiveis(
            @RequestParam(required = false) String cidade,
            @RequestParam(required = false) String tag,
            @RequestParam(required = false) Integer compatibilidadeMinima,
            Authentication auth) {
        return ResponseEntity.ok(mentoriaService.listarArtistasDisponiveis(
                auth.getName(), cidade, tag, compatibilidadeMinima));
    }

    @PostMapping("/artistas/{artistaId}/selecionar")
    ResponseEntity<MentoriaResponseDTO> selecionarArtista(
            @PathVariable UUID artistaId,
            Authentication auth) {
        return ResponseEntity.ok(mentoriaService.selecionarArtista(auth.getName(), artistaId));
    }

    @GetMapping("/minhas")
    ResponseEntity<MinhasMentoriasResponseDTO> listarMinhasMentorias(Authentication auth) {
        return ResponseEntity.ok(mentoriaService.listarMinhasMentorias(auth.getName()));
    }

    @PatchMapping("/{mentoriaId}/encerrar")
    ResponseEntity<MentoriaResponseDTO> encerrarMentoria(
            @PathVariable UUID mentoriaId,
            Authentication auth) {
        return ResponseEntity.ok(mentoriaService.encerrarMentoria(auth.getName(), mentoriaId));
    }
}

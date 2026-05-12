package com.flowcarreiras.flowcarreiras_api.controller;

import com.flowcarreiras.flowcarreiras_api.dto.ObraResponseDTO;
import com.flowcarreiras.flowcarreiras_api.dto.PortfolioPublicoDTO;
import com.flowcarreiras.flowcarreiras_api.exception.ObraNaoEncontradaException;
import com.flowcarreiras.flowcarreiras_api.model.PerfilArtista;
import com.flowcarreiras.flowcarreiras_api.model.enums.StatusObra;
import com.flowcarreiras.flowcarreiras_api.repository.ObraRepository;
import com.flowcarreiras.flowcarreiras_api.repository.PerfilArtistaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/obras/publico")
@RequiredArgsConstructor
public class PortfolioPublicoController {

    private final PerfilArtistaRepository perfilArtistaRepository;
    private final ObraRepository obraRepository;

    @GetMapping("/{urlPublica}")
    @Transactional(readOnly = true)
    ResponseEntity<PortfolioPublicoDTO> buscarPortfolio(@PathVariable String urlPublica) {
        PerfilArtista perfil = perfilArtistaRepository.findByUrlPublica(urlPublica)
                .orElseThrow(() -> new ObraNaoEncontradaException(urlPublica));

        List<ObraResponseDTO> obras = obraRepository
                .findByArtistaIdAndStatusOrderByDataPublicacaoDesc(perfil.getId(), StatusObra.PUBLICADA)
                .stream()
                .map(ObraResponseDTO::from)
                .toList();

        return ResponseEntity.ok(PortfolioPublicoDTO.builder()
                .artista(PortfolioPublicoDTO.ArtistaResumoDTO.from(perfil))
                .obras(obras)
                .build());
    }
}

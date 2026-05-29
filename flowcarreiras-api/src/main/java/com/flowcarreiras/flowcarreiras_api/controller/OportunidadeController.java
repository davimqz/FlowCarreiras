package com.flowcarreiras.flowcarreiras_api.controller;

import com.flowcarreiras.flowcarreiras_api.dto.oportunidades.OportunidadeResponseDTO;
import com.flowcarreiras.flowcarreiras_api.service.OportunidadeService;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/oportunidades")
@RequiredArgsConstructor
public class OportunidadeController {

    private final OportunidadeService oportunidadeService;

    @GetMapping
    List<OportunidadeResponseDTO> listar(
            @RequestParam(required = false) String tipos,
            @RequestParam(required = false) String tags,
            @RequestParam(required = false) String area,
            @RequestParam(required = false) String localidade,
            @RequestParam(required = false, name = "q") String query,
            @RequestParam(defaultValue = "50") int limit,
            @RequestParam(defaultValue = "0") int offset
    ) {
        OportunidadeService.Filtro filtro = new OportunidadeService.Filtro(
                splitLower(tipos),
                splitLower(tags),
                area,
                localidade,
                query,
                Math.min(Math.max(limit, 1), 200),
                Math.max(offset, 0)
        );
        return oportunidadeService.listar(filtro);
    }

    private Set<String> splitLower(String raw) {
        if (!StringUtils.hasText(raw)) return Set.of();
        return Arrays.stream(raw.split(","))
                .map(String::trim)
                .filter(StringUtils::hasText)
                .map(s -> s.toLowerCase(Locale.ROOT))
                .collect(Collectors.toSet());
    }
}

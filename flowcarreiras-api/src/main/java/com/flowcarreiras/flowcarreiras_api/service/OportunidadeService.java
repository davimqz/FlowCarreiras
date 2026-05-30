package com.flowcarreiras.flowcarreiras_api.service;

import com.flowcarreiras.flowcarreiras_api.dto.oportunidades.OportunidadeResponseDTO;
import com.flowcarreiras.flowcarreiras_api.model.Oportunidade;
import com.flowcarreiras.flowcarreiras_api.model.enums.OportunidadeStatus;
import com.flowcarreiras.flowcarreiras_api.model.enums.OportunidadeTipo;
import com.flowcarreiras.flowcarreiras_api.repository.OportunidadeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class OportunidadeService {

    private final OportunidadeRepository oportunidadeRepository;

    public List<OportunidadeResponseDTO> listar(Filtro filtro) {
        LocalDate hoje = LocalDate.now();
        List<Oportunidade> base = oportunidadeRepository
                .findByStatusAndDataEncerramentoGreaterThanEqualOrderByDataEncerramentoAsc(
                        OportunidadeStatus.ATIVA,
                        hoje
                );

        LocalDate limiteFim = calcularFimPrazo(hoje, filtro.prazo());
        List<OportunidadeResponseDTO> out = new ArrayList<>();

        for (Oportunidade oportunidade : base) {
            if (!filtro.tipos().isEmpty() && !filtro.tipos().contains(oportunidade.getTipo())) continue;
            if (StringUtils.hasText(filtro.area()) && !containsIgnoreCase(oportunidade.getAreaArtistica(), filtro.area())) continue;
            if (limiteFim != null && oportunidade.getDataEncerramento().isAfter(limiteFim)) continue;
            if (!filtro.tags().isEmpty() && !matchAllTags(oportunidade, filtro.tags())) continue;
            out.add(toResponse(oportunidade));
        }

        int start = Math.min(filtro.offset(), out.size());
        int end = Math.min(start + filtro.limit(), out.size());
        return out.subList(start, end);
    }

    public Oportunidade salvar(Oportunidade oportunidade) {
        return oportunidadeRepository.save(oportunidade);
    }

    @Scheduled(cron = "0 0 2 * * *")
    @Transactional
    public void expirarOportunidades() {
        oportunidadeRepository.expirarAntesDe(
                LocalDate.now(),
                OportunidadeStatus.ATIVA,
                OportunidadeStatus.EXPIRADA
        );
    }

    private LocalDate calcularFimPrazo(LocalDate hoje, PrazoFiltro prazo) {
        if (prazo == null || prazo == PrazoFiltro.SEM_FILTRO) return null;
        return switch (prazo) {
            case ESTA_SEMANA -> hoje.plusDays(7);
            case ESTE_MES -> hoje.with(TemporalAdjusters.lastDayOfMonth());
            default -> null;
        };
    }

    private boolean matchAllTags(Oportunidade oportunidade, Set<String> filtro) {
        if (oportunidade.getTags() == null || oportunidade.getTags().isEmpty()) return false;
        Set<String> tags = new HashSet<>();
        oportunidade.getTags().forEach(tag -> {
            if (tag.getNome() != null) tags.add(tag.getNome().toLowerCase(Locale.ROOT));
        });
        return tags.containsAll(filtro);
    }

    private boolean containsIgnoreCase(String valor, String busca) {
        if (!StringUtils.hasText(valor)) return false;
        return valor.toLowerCase(Locale.ROOT).contains(busca.toLowerCase(Locale.ROOT));
    }

    private OportunidadeResponseDTO toResponse(Oportunidade oportunidade) {
        return OportunidadeResponseDTO.builder()
                .id(oportunidade.getId().toString())
                .titulo(oportunidade.getTitulo())
                .descricao(oportunidade.getDescricao())
                .tipo(oportunidade.getTipo().name())
                .dataEncerramento(oportunidade.getDataEncerramento())
                .linkExterno(oportunidade.getLinkExterno())
                .areaArtistica(oportunidade.getAreaArtistica())
                .tags(oportunidade.getTags().stream().map(tag -> tag.getNome()).toList())
                .build();
    }

    public record Filtro(Set<OportunidadeTipo> tipos, Set<String> tags, String area, PrazoFiltro prazo, int limit, int offset) {
        public static Filtro vazio() {
            return new Filtro(Set.of(), Set.of(), null, PrazoFiltro.SEM_FILTRO, 50, 0);
        }
    }

    public enum PrazoFiltro {
        ESTA_SEMANA,
        ESTE_MES,
        SEM_FILTRO
    }
}
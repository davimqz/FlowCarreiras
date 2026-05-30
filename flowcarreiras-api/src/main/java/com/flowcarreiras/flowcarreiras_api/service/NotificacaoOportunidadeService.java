package com.flowcarreiras.flowcarreiras_api.service;

import com.flowcarreiras.flowcarreiras_api.dto.notificacoes.NotificacaoOportunidadeResponseDTO;
import com.flowcarreiras.flowcarreiras_api.dto.notificacoes.NotificacoesResumoResponseDTO;
import com.flowcarreiras.flowcarreiras_api.exception.AcessoNegadoException;
import com.flowcarreiras.flowcarreiras_api.model.NotificacaoOportunidade;
import com.flowcarreiras.flowcarreiras_api.model.Oportunidade;
import com.flowcarreiras.flowcarreiras_api.model.PerfilArtista;
import com.flowcarreiras.flowcarreiras_api.model.Tag;
import com.flowcarreiras.flowcarreiras_api.repository.NotificacaoOportunidadeRepository;
import com.flowcarreiras.flowcarreiras_api.repository.PerfilArtistaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NotificacaoOportunidadeService {

    private static final int LIMITE_DIARIO = 3;

    private final NotificacaoOportunidadeRepository notificacaoRepository;
    private final PerfilArtistaRepository perfilArtistaRepository;

    @Transactional(readOnly = true)
    public NotificacoesResumoResponseDTO listarResumo(String email, int limit) {
        PerfilArtista perfil = buscarPerfil(email);
        List<NotificacaoOportunidadeResponseDTO> notificacoes = notificacaoRepository
                .findTop20ByPerfilIdOrderByDataCriacaoDesc(perfil.getId())
                .stream()
                .limit(Math.max(1, limit))
                .map(this::toResponse)
                .toList();

        long totalNaoLidas = notificacaoRepository.countByPerfilIdAndLidaFalse(perfil.getId());

        return NotificacoesResumoResponseDTO.builder()
                .totalNaoLidas(totalNaoLidas)
                .notificacoes(notificacoes)
                .build();
    }

    @Transactional
    public void marcarComoLida(UUID id, String email) {
        PerfilArtista perfil = buscarPerfil(email);
        NotificacaoOportunidade notificacao = notificacaoRepository.findById(id)
                .orElseThrow(AcessoNegadoException::new);
        if (!notificacao.getPerfil().getId().equals(perfil.getId())) {
            throw new AcessoNegadoException();
        }
        notificacao.setLida(true);
        notificacaoRepository.save(notificacao);
    }

    @Transactional
    public void notificarNovaOportunidade(Oportunidade oportunidade) {
        if (oportunidade == null || oportunidade.getDataEncerramento() == null) return;
        if (oportunidade.getDataEncerramento().isBefore(LocalDate.now().plusDays(1))) return;
        Set<Tag> tags = oportunidade.getTags();
        if (tags == null || tags.isEmpty()) return;

        List<PerfilArtista> perfis = perfilArtistaRepository.listarPerfisParaNotificacoes(tags);
        LocalDateTime inicio = LocalDate.now().atStartOfDay();
        LocalDateTime fim = inicio.plusDays(1);

        for (PerfilArtista perfil : perfis) {
            if (notificacaoRepository.existsByPerfilIdAndOportunidadeId(perfil.getId(), oportunidade.getId())) continue;
            long totalHoje = notificacaoRepository.countByPerfilIdAndDataCriacaoBetween(perfil.getId(), inicio, fim);
            if (totalHoje >= LIMITE_DIARIO) continue;

            NotificacaoOportunidade notificacao = NotificacaoOportunidade.builder()
                    .perfil(perfil)
                    .oportunidade(oportunidade)
                    .titulo(oportunidade.getTitulo())
                    .tipo(oportunidade.getTipo().name())
                    .dataEncerramento(oportunidade.getDataEncerramento())
                    .lida(false)
                    .build();

            notificacaoRepository.save(notificacao);
        }
    }

    private NotificacaoOportunidadeResponseDTO toResponse(NotificacaoOportunidade notificacao) {
        return NotificacaoOportunidadeResponseDTO.builder()
                .id(notificacao.getId())
                .oportunidadeId(notificacao.getOportunidade().getId())
                .titulo(notificacao.getTitulo())
                .tipo(notificacao.getTipo())
                .dataEncerramento(notificacao.getDataEncerramento())
                .lida(notificacao.getLida())
                .dataCriacao(notificacao.getDataCriacao())
                .build();
    }

    private PerfilArtista buscarPerfil(String email) {
        return perfilArtistaRepository.findByUsuarioEmail(email)
                .orElseThrow(AcessoNegadoException::new);
    }
}

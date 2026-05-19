package com.flowcarreiras.flowcarreiras_api.service;

import com.flowcarreiras.flowcarreiras_api.dto.mentoria.ArtistaDisponivelMentoriaDTO;
import com.flowcarreiras.flowcarreiras_api.dto.mentoria.MentoriaConfiguracaoRequestDTO;
import com.flowcarreiras.flowcarreiras_api.dto.mentoria.MentoriaConfiguracaoResponseDTO;
import com.flowcarreiras.flowcarreiras_api.dto.mentoria.MentoriaResponseDTO;
import com.flowcarreiras.flowcarreiras_api.dto.mentoria.MentorResumoDTO;
import com.flowcarreiras.flowcarreiras_api.dto.mentoria.MinhasMentoriasResponseDTO;
import com.flowcarreiras.flowcarreiras_api.dto.mentoria.PerfilMentoriaResumoDTO;
import com.flowcarreiras.flowcarreiras_api.exception.AcessoNegadoException;
import com.flowcarreiras.flowcarreiras_api.model.Mentoria;
import com.flowcarreiras.flowcarreiras_api.model.PerfilArtista;
import com.flowcarreiras.flowcarreiras_api.model.enums.ModalidadeMentoria;
import com.flowcarreiras.flowcarreiras_api.model.enums.StatusMentoria;
import com.flowcarreiras.flowcarreiras_api.repository.MentoriaRepository;
import com.flowcarreiras.flowcarreiras_api.repository.PerfilArtistaRepository;
import com.flowcarreiras.flowcarreiras_api.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MentoriaService {

    private final PerfilArtistaRepository perfilArtistaRepository;
    private final TagRepository tagRepository;
    private final MentoriaRepository mentoriaRepository;

    @Transactional(readOnly = true)
    public MentoriaConfiguracaoResponseDTO obterConfiguracao(String email) {
        return MentoriaConfiguracaoResponseDTO.from(buscarPerfilPorEmail(email));
    }

    @Transactional
    public MentoriaConfiguracaoResponseDTO salvarConfiguracao(String email, MentoriaConfiguracaoRequestDTO dto) {
        PerfilArtista perfil = buscarPerfilPorEmail(email);

        perfil.setMentoriaGratuita(dto.getMentoriaGratuita());
        perfil.setValorHoraMentoria(Boolean.TRUE.equals(dto.getMentoriaGratuita()) ? null : dto.getValorHoraMentoria());
        perfil.setDescricaoMentoria(dto.getDescricaoMentoria().trim());
        perfil.setModalidadeMentoria(parseModalidade(dto.getModalidadeMentoria()));
        perfil.setCidadeMentoria(normalizarCidade(dto.getCidadeMentoria()));

        if (dto.getTagsExpertiseIds() != null) {
            perfil.setTagsExpertise(new HashSet<>(tagRepository.findAllById(dto.getTagsExpertiseIds())));
        }

        validarConfiguracao(perfil);

        boolean primeiraConfiguracao = !Boolean.TRUE.equals(perfil.getPerfilMentorConfigurado());
        perfil.setPerfilMentorConfigurado(true);
        if (dto.getDisponivelParaMentorar() != null) {
            perfil.setDisponivelParaMentorar(dto.getDisponivelParaMentorar());
        } else if (primeiraConfiguracao) {
            perfil.setDisponivelParaMentorar(true);
        }

        return MentoriaConfiguracaoResponseDTO.from(perfilArtistaRepository.save(perfil));
    }

    @Transactional
    public MentoriaConfiguracaoResponseDTO ativar(String email) {
        PerfilArtista perfil = buscarPerfilPorEmail(email);
        validarConfiguracao(perfil);
        perfil.setPerfilMentorConfigurado(true);
        perfil.setDisponivelParaMentorar(true);
        return MentoriaConfiguracaoResponseDTO.from(perfilArtistaRepository.save(perfil));
    }

    @Transactional
    public MentoriaConfiguracaoResponseDTO pausar(String email) {
        PerfilArtista perfil = buscarPerfilPorEmail(email);
        perfil.setDisponivelParaMentorar(false);
        return MentoriaConfiguracaoResponseDTO.from(perfilArtistaRepository.save(perfil));
    }

    @Transactional(readOnly = true)
    public List<ArtistaDisponivelMentoriaDTO> listarArtistasDisponiveis(
            String email,
            String cidade,
            String tag,
            Integer compatibilidadeMinima) {
        PerfilArtista mentor = buscarMentorAtivo(email);
        String filtroCidade = normalizarFiltro(cidade);
        String filtroTag = normalizarFiltro(tag);
        int compatMinima = compatibilidadeMinima == null ? 0 : Math.max(0, Math.min(100, compatibilidadeMinima));

        return perfilArtistaRepository
                .listarArtistasSemMentoriaAtiva(mentor.getId(), StatusMentoria.ATIVA)
                .stream()
                .map(artista -> ArtistaDisponivelMentoriaDTO.from(artista, mentor))
                .filter(artista -> filtroCidade == null || contem(artista.getCidade(), filtroCidade))
                .filter(artista -> filtroTag == null || artista.getTagsNecessidade().stream()
                        .anyMatch(t -> contem(t.getNome(), filtroTag)))
                .filter(artista -> artista.getPercentualCompatibilidade() >= compatMinima)
                .sorted(Comparator
                        .comparing(ArtistaDisponivelMentoriaDTO::getQuantidadeTagsCompativeis).reversed()
                        .thenComparing(ArtistaDisponivelMentoriaDTO::getPercentualCompatibilidade, Comparator.reverseOrder())
                        .thenComparing(ArtistaDisponivelMentoriaDTO::getNome))
                .toList();
    }

    @Transactional(readOnly = true)
    public List<MentorResumoDTO> listarMentoresAtivos(
            String cidade,
            String modalidade,
            Boolean gratuita,
            String tag,
            String area) {
        String filtroCidade = normalizarFiltro(cidade);
        String filtroModalidade = normalizarFiltro(modalidade);
        String filtroTag = normalizarFiltro(tag);
        String filtroArea = normalizarFiltro(area);

        return perfilArtistaRepository.listarMentoresAtivos()
                .stream()
                .map(MentorResumoDTO::from)
                .filter(mentor -> filtroCidade == null
                        || contem(mentor.getCidade(), filtroCidade)
                        || contem(mentor.getCidadeMentoria(), filtroCidade))
                .filter(mentor -> filtroModalidade == null
                        || contem(mentor.getModalidadeMentoria(), filtroModalidade))
                .filter(mentor -> gratuita == null || gratuita.equals(mentor.getMentoriaGratuita()))
                .filter(mentor -> filtroTag == null || mentor.getTagsExpertise().stream()
                        .anyMatch(t -> contem(t.getNome(), filtroTag)))
                .filter(mentor -> filtroArea == null
                        || contem(mentor.getAreaArtisticaPrincipal(), filtroArea)
                        || mentor.getTagsExpertise().stream().anyMatch(t -> contem(t.getNome(), filtroArea)))
                .toList();
    }

    @Transactional
    public MentoriaResponseDTO selecionarArtista(String email, UUID artistaId) {
        PerfilArtista mentor = buscarMentorAtivo(email);
        PerfilArtista artista = perfilArtistaRepository.buscarPorIdComLock(artistaId)
                .orElseThrow(() -> new IllegalArgumentException("Artista nao encontrado"));

        if (mentor.getId().equals(artista.getId())) {
            throw new IllegalArgumentException("Mentor nao pode selecionar o proprio perfil");
        }
        if (!Boolean.TRUE.equals(artista.getUsuario().getAtivo())) {
            throw new IllegalArgumentException("Artista nao esta ativo na plataforma");
        }
        if (mentoriaRepository.existsByArtistaIdAndStatus(artista.getId(), StatusMentoria.ATIVA)) {
            throw new IllegalArgumentException("Este artista ja possui uma mentoria ativa");
        }

        Mentoria mentoria = Mentoria.builder()
                .mentor(mentor)
                .artista(artista)
                .status(StatusMentoria.ATIVA)
                .build();

        return MentoriaResponseDTO.from(mentoriaRepository.save(mentoria));
    }

    @Transactional(readOnly = true)
    public MinhasMentoriasResponseDTO listarMinhasMentorias(String email) {
        PerfilArtista perfil = buscarPerfilPorEmail(email);
        return MinhasMentoriasResponseDTO.builder()
                .comoMentor(mentoriaRepository.findByMentorIdOrderByDataCriacaoDesc(perfil.getId())
                        .stream()
                        .map(MentoriaResponseDTO::from)
                        .toList())
                .comoArtista(mentoriaRepository.findByArtistaIdOrderByDataCriacaoDesc(perfil.getId())
                        .stream()
                        .map(MentoriaResponseDTO::from)
                        .toList())
                .build();
    }

    @Transactional
    public MentoriaResponseDTO encerrarMentoria(String email, UUID mentoriaId) {
        PerfilArtista mentor = buscarPerfilPorEmail(email);
        Mentoria mentoria = mentoriaRepository.findByIdAndMentorId(mentoriaId, mentor.getId())
                .orElseThrow(() -> new AcessoNegadoException("Mentoria nao encontrada para este mentor"));

        if (mentoria.getStatus() != StatusMentoria.ATIVA) {
            throw new IllegalArgumentException("A mentoria ja esta encerrada ou cancelada");
        }

        mentoria.setStatus(StatusMentoria.ENCERRADA);
        mentoria.setDataEncerramento(LocalDateTime.now());
        return MentoriaResponseDTO.from(mentoriaRepository.save(mentoria));
    }

    private PerfilArtista buscarPerfilPorEmail(String email) {
        return perfilArtistaRepository.findByUsuarioEmail(email)
                .orElseThrow(AcessoNegadoException::new);
    }

    private PerfilArtista buscarMentorAtivo(String email) {
        PerfilArtista mentor = buscarPerfilPorEmail(email);
        if (!Boolean.TRUE.equals(mentor.getPerfilMentorConfigurado())) {
            throw new IllegalArgumentException("Configure seus dados de mentoria antes de selecionar artistas");
        }
        validarConfiguracao(mentor);
        if (!Boolean.TRUE.equals(mentor.getDisponivelParaMentorar())) {
            throw new IllegalArgumentException("Ative sua mentoria para selecionar artistas");
        }
        return mentor;
    }

    private ModalidadeMentoria parseModalidade(String modalidade) {
        try {
            return ModalidadeMentoria.valueOf(modalidade.trim().toUpperCase());
        } catch (RuntimeException ex) {
            throw new IllegalArgumentException("Modalidade de mentoria invalida");
        }
    }

    private String normalizarCidade(String cidade) {
        return cidade == null || cidade.isBlank() ? null : cidade.trim();
    }

    private String normalizarFiltro(String valor) {
        return valor == null || valor.isBlank() ? null : valor.trim().toLowerCase(Locale.ROOT);
    }

    private boolean contem(String valor, String filtro) {
        return valor != null && valor.toLowerCase(Locale.ROOT).contains(filtro);
    }

    private void validarConfiguracao(PerfilArtista perfil) {
        if (perfil.getDescricaoMentoria() == null || perfil.getDescricaoMentoria().isBlank()) {
            throw new IllegalArgumentException("Descricao da mentoria e obrigatoria");
        }
        if (perfil.getModalidadeMentoria() == null) {
            throw new IllegalArgumentException("Modalidade da mentoria e obrigatoria");
        }
        if (!Boolean.TRUE.equals(perfil.getMentoriaGratuita())
                && (perfil.getValorHoraMentoria() == null || perfil.getValorHoraMentoria().compareTo(BigDecimal.ZERO) <= 0)) {
            throw new IllegalArgumentException("Valor por hora e obrigatorio para mentorias pagas");
        }
        if ((perfil.getModalidadeMentoria() == ModalidadeMentoria.PRESENCIAL
                || perfil.getModalidadeMentoria() == ModalidadeMentoria.HIBRIDA)
                && (perfil.getCidadeMentoria() == null || perfil.getCidadeMentoria().isBlank())) {
            throw new IllegalArgumentException("Cidade e obrigatoria para mentoria presencial ou hibrida");
        }
        if (perfil.getTagsExpertise() == null || perfil.getTagsExpertise().isEmpty()) {
            throw new IllegalArgumentException("Adicione ao menos uma tag de expertise para atuar como mentor");
        }
    }
}

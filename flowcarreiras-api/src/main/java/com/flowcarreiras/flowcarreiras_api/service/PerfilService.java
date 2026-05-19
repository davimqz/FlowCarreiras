package com.flowcarreiras.flowcarreiras_api.service;

import com.flowcarreiras.flowcarreiras_api.dto.AtualizarPerfilRequestDTO;
import com.flowcarreiras.flowcarreiras_api.dto.PerfilCompletoResponseDTO;
import com.flowcarreiras.flowcarreiras_api.dto.TagDTO;
import com.flowcarreiras.flowcarreiras_api.dto.onboarding.OnboardingStatusResponseDTO;
import com.flowcarreiras.flowcarreiras_api.exception.AcessoNegadoException;
import com.flowcarreiras.flowcarreiras_api.model.PerfilArtista;
import com.flowcarreiras.flowcarreiras_api.model.Tag;
import com.flowcarreiras.flowcarreiras_api.model.enums.StatusEtapaOnboarding;
import com.flowcarreiras.flowcarreiras_api.repository.PerfilArtistaRepository;
import com.flowcarreiras.flowcarreiras_api.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PerfilService {

    private final PerfilArtistaRepository perfilArtistaRepository;
    private final TagRepository tagRepository;
    private final FileStorageService fileStorageService;

    @Transactional(readOnly = true)
    public PerfilCompletoResponseDTO obterPerfilCompleto(String email) {
        return toPerfilCompleto(buscarPerfilPorEmail(email));
    }

    @Transactional
    public PerfilCompletoResponseDTO atualizarPerfil(String email, AtualizarPerfilRequestDTO dto) {
        PerfilArtista perfil = buscarPerfilPorEmail(email);

        if (dto.getBio() != null)
            perfil.setBio(dto.getBio().isBlank() ? null : dto.getBio());
        if (dto.getCidade() != null)
            perfil.setCidade(dto.getCidade().isBlank() ? null : dto.getCidade());
        if (dto.getAreaArtisticaPrincipal() != null)
            perfil.setAreaArtisticaPrincipal(dto.getAreaArtisticaPrincipal().isBlank() ? null : dto.getAreaArtisticaPrincipal());
        if (dto.getTagsExpertiseIds() != null)
            perfil.setTagsExpertise(new HashSet<>(tagRepository.findAllById(dto.getTagsExpertiseIds())));
        if (dto.getTagsNecessidadeIds() != null)
            perfil.setTagsNecessidade(new HashSet<>(tagRepository.findAllById(dto.getTagsNecessidadeIds())));

        perfil.setPercentualCompletude(perfil.calcularPercentualCompletude());
        return toPerfilCompleto(perfilArtistaRepository.save(perfil));
    }

    @Transactional
    public PerfilCompletoResponseDTO atualizarFotoPerfil(String email, MultipartFile file) {
        PerfilArtista perfil = buscarPerfilPorEmail(email);
        if (perfil.getFotoPerfil() != null) {
            fileStorageService.deletar(perfil.getFotoPerfil());
        }
        perfil.setFotoPerfil(fileStorageService.salvarFotoPerfil(file));
        perfil.setPercentualCompletude(perfil.calcularPercentualCompletude());
        return toPerfilCompleto(perfilArtistaRepository.save(perfil));
    }

    // --- Métodos de onboarding (mantidos) ---

    @Transactional
    public OnboardingStatusResponseDTO salvarEtapaArea(String email, String area) {
        PerfilArtista perfil = buscarPerfilPorEmail(email);
        perfil.setAreaArtisticaPrincipal(area);
        perfil.setStatusEtapaArea(StatusEtapaOnboarding.CONCLUIDA);
        perfil.setPercentualCompletude(perfil.calcularPercentualCompletude());
        return toOnboardingStatus(perfilArtistaRepository.save(perfil));
    }

    @Transactional
    public OnboardingStatusResponseDTO salvarEtapaCidade(String email, String cidade) {
        PerfilArtista perfil = buscarPerfilPorEmail(email);
        perfil.setCidade(cidade);
        perfil.setStatusEtapaCidade(StatusEtapaOnboarding.CONCLUIDA);
        perfil.setPercentualCompletude(perfil.calcularPercentualCompletude());
        return toOnboardingStatus(perfilArtistaRepository.save(perfil));
    }

    @Transactional
    public OnboardingStatusResponseDTO salvarEtapaBio(String email, String bio) {
        PerfilArtista perfil = buscarPerfilPorEmail(email);
        perfil.setBio(bio);
        perfil.setStatusEtapaBio(StatusEtapaOnboarding.CONCLUIDA);
        perfil.setPercentualCompletude(perfil.calcularPercentualCompletude());
        return toOnboardingStatus(perfilArtistaRepository.save(perfil));
    }

    @Transactional
    public OnboardingStatusResponseDTO salvarEtapaTags(String email, List<UUID> tagNecessidadeIds) {
        PerfilArtista perfil = buscarPerfilPorEmail(email);
        Set<Tag> tags = new HashSet<>(tagRepository.findAllById(tagNecessidadeIds));
        perfil.setTagsNecessidade(tags);
        perfil.setStatusEtapaTags(StatusEtapaOnboarding.CONCLUIDA);
        perfil.setPercentualCompletude(perfil.calcularPercentualCompletude());
        return toOnboardingStatus(perfilArtistaRepository.save(perfil));
    }

    @Transactional
    public OnboardingStatusResponseDTO pularEtapa(String email, String etapa) {
        PerfilArtista perfil = buscarPerfilPorEmail(email);
        switch (etapa) {
            case "area"   -> perfil.setStatusEtapaArea(StatusEtapaOnboarding.PULADA);
            case "cidade" -> perfil.setStatusEtapaCidade(StatusEtapaOnboarding.PULADA);
            case "bio"    -> perfil.setStatusEtapaBio(StatusEtapaOnboarding.PULADA);
            case "tags"   -> perfil.setStatusEtapaTags(StatusEtapaOnboarding.PULADA);
            default -> throw new IllegalArgumentException("Etapa inválida: " + etapa);
        }
        return toOnboardingStatus(perfilArtistaRepository.save(perfil));
    }

    @Transactional
    public OnboardingStatusResponseDTO concluirOnboarding(String email) {
        PerfilArtista perfil = buscarPerfilPorEmail(email);
        if (perfil.getStatusEtapaArea() == StatusEtapaOnboarding.PENDENTE)
            perfil.setStatusEtapaArea(StatusEtapaOnboarding.PULADA);
        if (perfil.getStatusEtapaCidade() == StatusEtapaOnboarding.PENDENTE)
            perfil.setStatusEtapaCidade(StatusEtapaOnboarding.PULADA);
        if (perfil.getStatusEtapaBio() == StatusEtapaOnboarding.PENDENTE)
            perfil.setStatusEtapaBio(StatusEtapaOnboarding.PULADA);
        if (perfil.getStatusEtapaTags() == StatusEtapaOnboarding.PENDENTE)
            perfil.setStatusEtapaTags(StatusEtapaOnboarding.PULADA);
        perfil.setOnboardingConcluido(true);
        return toOnboardingStatus(perfilArtistaRepository.save(perfil));
    }

    // --- helpers privados ---

    private PerfilArtista buscarPerfilPorEmail(String email) {
        return perfilArtistaRepository.findByUsuarioEmail(email)
                .orElseThrow(AcessoNegadoException::new);
    }

    private PerfilCompletoResponseDTO toPerfilCompleto(PerfilArtista perfil) {
        return PerfilCompletoResponseDTO.builder()
                .id(perfil.getId())
                .nome(perfil.getUsuario().getNome())
                .urlPublica(perfil.getUrlPublica())
                .bio(perfil.getBio())
                .fotoPerfil(perfil.getFotoPerfil())
                .cidade(perfil.getCidade())
                .areaArtisticaPrincipal(perfil.getAreaArtisticaPrincipal())
                .disponivelParaMentorar(perfil.getDisponivelParaMentorar())
                .perfilMentorConfigurado(perfil.getPerfilMentorConfigurado())
                .mentoriaGratuita(perfil.getMentoriaGratuita())
                .valorHoraMentoria(perfil.getValorHoraMentoria())
                .descricaoMentoria(perfil.getDescricaoMentoria())
                .modalidadeMentoria(perfil.getModalidadeMentoria() != null ? perfil.getModalidadeMentoria().name() : null)
                .cidadeMentoria(perfil.getCidadeMentoria())
                .percentualCompletude(perfil.getPercentualCompletude())
                .onboardingConcluido(perfil.getOnboardingConcluido())
                .totalObras(perfil.getObras().size())
                .tagsExpertise(perfil.getTagsExpertise().stream().map(TagDTO::from).toList())
                .tagsNecessidade(perfil.getTagsNecessidade().stream().map(TagDTO::from).toList())
                .statusEtapaArea(perfil.getStatusEtapaArea().name())
                .statusEtapaCidade(perfil.getStatusEtapaCidade().name())
                .statusEtapaBio(perfil.getStatusEtapaBio().name())
                .statusEtapaTags(perfil.getStatusEtapaTags().name())
                .build();
    }

    private OnboardingStatusResponseDTO toOnboardingStatus(PerfilArtista perfil) {
        return OnboardingStatusResponseDTO.builder()
                .onboardingConcluido(perfil.getOnboardingConcluido())
                .statusEtapaArea(perfil.getStatusEtapaArea().name())
                .statusEtapaCidade(perfil.getStatusEtapaCidade().name())
                .statusEtapaBio(perfil.getStatusEtapaBio().name())
                .statusEtapaTags(perfil.getStatusEtapaTags().name())
                .percentualCompletude(perfil.getPercentualCompletude())
                .areaArtisticaPrincipal(perfil.getAreaArtisticaPrincipal())
                .cidade(perfil.getCidade())
                .bio(perfil.getBio())
                .build();
    }
}

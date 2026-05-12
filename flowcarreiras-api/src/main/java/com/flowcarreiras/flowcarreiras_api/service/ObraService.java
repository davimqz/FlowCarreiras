package com.flowcarreiras.flowcarreiras_api.service;

import com.flowcarreiras.flowcarreiras_api.dto.ObraRequestDTO;
import com.flowcarreiras.flowcarreiras_api.dto.ObraResponseDTO;
import com.flowcarreiras.flowcarreiras_api.exception.AcessoNegadoException;
import com.flowcarreiras.flowcarreiras_api.exception.ArquivoInvalidoException;
import com.flowcarreiras.flowcarreiras_api.exception.ObraNaoEncontradaException;
import com.flowcarreiras.flowcarreiras_api.model.Obra;
import com.flowcarreiras.flowcarreiras_api.model.PerfilArtista;
import com.flowcarreiras.flowcarreiras_api.model.Tag;
import com.flowcarreiras.flowcarreiras_api.model.enums.StatusObra;
import com.flowcarreiras.flowcarreiras_api.model.enums.TipoMidia;
import com.flowcarreiras.flowcarreiras_api.repository.ObraRepository;
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
public class ObraService {

    private static final Set<String> DOMINIOS_EMBED = Set.of(
        "youtube.com", "youtu.be", "vimeo.com"
    );

    private final ObraRepository obraRepository;
    private final PerfilArtistaRepository perfilArtistaRepository;
    private final TagRepository tagRepository;
    private final FileStorageService fileStorageService;

    @Transactional
    public ObraResponseDTO criarObra(ObraRequestDTO dto, MultipartFile file, String emailArtista) {
        PerfilArtista perfil = buscarPerfilPorEmail(emailArtista);
        Set<Tag> tags = resolverTags(dto.getTagIds());
        String urlMidia = resolverUrlMidia(dto, file);

        Obra obra = Obra.builder()
                .titulo(dto.getTitulo())
                .descricao(dto.getDescricao())
                .tipoMidia(dto.getTipoMidia())
                .urlMidia(urlMidia)
                .status(dto.getStatus())
                .artista(perfil)
                .tags(tags)
                .build();

        return ObraResponseDTO.from(obraRepository.save(obra));
    }

    @Transactional
    public ObraResponseDTO editarObra(UUID obraId, ObraRequestDTO dto, MultipartFile file, String emailArtista) {
        PerfilArtista perfil = buscarPerfilPorEmail(emailArtista);
        Obra obra = obraRepository.findByIdAndArtistaId(obraId, perfil.getId())
                .orElseThrow(() -> new ObraNaoEncontradaException(obraId));

        Set<Tag> tags = resolverTags(dto.getTagIds());
        if (tags.isEmpty()) {
            throw new ArquivoInvalidoException("Adicione ao menos uma tag antes de salvar");
        }

        // Substitui arquivo de mídia apenas se um novo foi enviado
        String urlMidia = obra.getUrlMidia();
        if (file != null && !file.isEmpty()) {
            fileStorageService.deletar(urlMidia);
            urlMidia = fileStorageService.salvar(file, dto.getTipoMidia());
        } else if (dto.getTipoMidia() == TipoMidia.EMBED) {
            urlMidia = validarEObterUrlEmbed(dto.getUrlMidia());
        }

        obra.setTitulo(dto.getTitulo());
        obra.setDescricao(dto.getDescricao());
        obra.setTipoMidia(dto.getTipoMidia());
        obra.setUrlMidia(urlMidia);
        obra.setStatus(dto.getStatus());
        obra.setTags(tags);

        return ObraResponseDTO.from(obraRepository.save(obra));
    }

    @Transactional
    public void removerObra(UUID obraId, String emailArtista) {
        PerfilArtista perfil = buscarPerfilPorEmail(emailArtista);
        Obra obra = obraRepository.findByIdAndArtistaId(obraId, perfil.getId())
                .orElseThrow(() -> new ObraNaoEncontradaException(obraId));

        fileStorageService.deletar(obra.getUrlMidia());
        obraRepository.delete(obra);
    }

    @Transactional(readOnly = true)
    public List<ObraResponseDTO> listarObrasPorEmail(String email) {
        PerfilArtista perfil = buscarPerfilPorEmail(email);
        return obraRepository.findByArtistaIdOrderByDataPublicacaoDesc(perfil.getId())
                .stream().map(ObraResponseDTO::from).toList();
    }

    @Transactional(readOnly = true)
    public List<ObraResponseDTO> listarObrasPorArtista(UUID artistaId, boolean somentePublicadas) {
        List<Obra> obras = somentePublicadas
                ? obraRepository.findByArtistaIdAndStatusOrderByDataPublicacaoDesc(artistaId, StatusObra.PUBLICADA)
                : obraRepository.findByArtistaIdOrderByDataPublicacaoDesc(artistaId);

        return obras.stream().map(ObraResponseDTO::from).toList();
    }

    @Transactional(readOnly = true)
    public ObraResponseDTO buscarPorId(UUID id) {
        return obraRepository.findById(id)
                .map(ObraResponseDTO::from)
                .orElseThrow(() -> new ObraNaoEncontradaException(id));
    }

    // --- helpers ---

    private PerfilArtista buscarPerfilPorEmail(String email) {
        return perfilArtistaRepository.findByUsuarioEmail(email)
                .orElseThrow(() -> new AcessoNegadoException());
    }

    private Set<Tag> resolverTags(List<UUID> tagIds) {
        if (tagIds == null || tagIds.isEmpty()) return new HashSet<>();
        return new HashSet<>(tagRepository.findAllById(tagIds));
    }

    private String resolverUrlMidia(ObraRequestDTO dto, MultipartFile file) {
        if (dto.getTipoMidia() == TipoMidia.EMBED) {
            return validarEObterUrlEmbed(dto.getUrlMidia());
        }
        if (file == null || file.isEmpty()) {
            throw new ArquivoInvalidoException("Arquivo de mídia é obrigatório para este tipo");
        }
        return fileStorageService.salvar(file, dto.getTipoMidia());
    }

    private String validarEObterUrlEmbed(String url) {
        if (url == null || url.isBlank()) {
            throw new ArquivoInvalidoException("URL do embed é obrigatória");
        }
        boolean valido = DOMINIOS_EMBED.stream().anyMatch(url::contains);
        if (!valido) {
            throw new ArquivoInvalidoException("URL embed deve ser do YouTube ou Vimeo");
        }
        return url;
    }
}

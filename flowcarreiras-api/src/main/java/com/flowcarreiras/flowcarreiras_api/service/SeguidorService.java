package com.flowcarreiras.flowcarreiras_api.service;

import com.flowcarreiras.flowcarreiras_api.dto.SeguidorStatusDTO;
import com.flowcarreiras.flowcarreiras_api.exception.AcessoNegadoException;
import com.flowcarreiras.flowcarreiras_api.exception.ObraNaoEncontradaException;
import com.flowcarreiras.flowcarreiras_api.model.Seguidor;
import com.flowcarreiras.flowcarreiras_api.model.Usuario;
import com.flowcarreiras.flowcarreiras_api.repository.SeguidorRepository;
import com.flowcarreiras.flowcarreiras_api.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SeguidorService {

    private final SeguidorRepository seguidorRepository;
    private final UsuarioRepository usuarioRepository;

    // Status público — contagens do alvo; "seguindoPeloUsuario" depende de haver alguém logado
    @Transactional(readOnly = true)
    public SeguidorStatusDTO obterStatus(UUID alvoId, String emailUsuario) {
        if (!usuarioRepository.existsById(alvoId)) {
            throw new ObraNaoEncontradaException(alvoId.toString());
        }
        boolean seguindo = false;
        if (emailUsuario != null) {
            Usuario usuario = usuarioRepository.findByEmail(emailUsuario).orElse(null);
            if (usuario != null) {
                seguindo = seguidorRepository.existsBySeguidorIdAndSeguidoId(usuario.getId(), alvoId);
            }
        }
        return montarStatus(alvoId, seguindo);
    }

    @Transactional
    public SeguidorStatusDTO seguir(UUID alvoId, String emailUsuario) {
        Usuario seguidor = usuarioRepository.findByEmail(emailUsuario)
                .orElseThrow(AcessoNegadoException::new);
        Usuario alvo = usuarioRepository.findById(alvoId)
                .orElseThrow(() -> new ObraNaoEncontradaException(alvoId.toString()));

        if (seguidor.getId().equals(alvo.getId())) {
            throw new IllegalArgumentException("Você não pode seguir a si mesmo");
        }

        // Idempotente: só insere se ainda não seguir
        if (!seguidorRepository.existsBySeguidorIdAndSeguidoId(seguidor.getId(), alvo.getId())) {
            seguidorRepository.save(Seguidor.builder()
                    .seguidor(seguidor)
                    .seguido(alvo)
                    .build());
        }
        return montarStatus(alvoId, true);
    }

    @Transactional
    public SeguidorStatusDTO deixarDeSeguir(UUID alvoId, String emailUsuario) {
        if (!usuarioRepository.existsById(alvoId)) {
            throw new ObraNaoEncontradaException(alvoId.toString());
        }
        Usuario seguidor = usuarioRepository.findByEmail(emailUsuario)
                .orElseThrow(AcessoNegadoException::new);

        seguidorRepository.deleteBySeguidorIdAndSeguidoId(seguidor.getId(), alvoId);
        return montarStatus(alvoId, false);
    }

    private SeguidorStatusDTO montarStatus(UUID alvoId, boolean seguindo) {
        return SeguidorStatusDTO.builder()
                .totalSeguidores(seguidorRepository.countBySeguidoId(alvoId))
                .totalSeguindo(seguidorRepository.countBySeguidorId(alvoId))
                .seguindoPeloUsuario(seguindo)
                .build();
    }
}

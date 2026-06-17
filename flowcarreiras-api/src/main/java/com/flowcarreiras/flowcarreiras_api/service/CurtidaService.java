package com.flowcarreiras.flowcarreiras_api.service;

import com.flowcarreiras.flowcarreiras_api.dto.CurtidaStatusDTO;
import com.flowcarreiras.flowcarreiras_api.exception.AcessoNegadoException;
import com.flowcarreiras.flowcarreiras_api.exception.ObraNaoEncontradaException;
import com.flowcarreiras.flowcarreiras_api.model.Curtida;
import com.flowcarreiras.flowcarreiras_api.model.Obra;
import com.flowcarreiras.flowcarreiras_api.model.Usuario;
import com.flowcarreiras.flowcarreiras_api.repository.CurtidaRepository;
import com.flowcarreiras.flowcarreiras_api.repository.ObraRepository;
import com.flowcarreiras.flowcarreiras_api.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CurtidaService {

    private final CurtidaRepository curtidaRepository;
    private final ObraRepository obraRepository;
    private final UsuarioRepository usuarioRepository;

    // Status público — total de curtidas; "curtidoPeloUsuario" depende de haver alguém logado
    @Transactional(readOnly = true)
    public CurtidaStatusDTO obterStatus(UUID obraId, String emailUsuario) {
        if (!obraRepository.existsById(obraId)) {
            throw new ObraNaoEncontradaException(obraId);
        }
        boolean curtido = false;
        if (emailUsuario != null) {
            Usuario usuario = usuarioRepository.findByEmail(emailUsuario).orElse(null);
            if (usuario != null) {
                curtido = curtidaRepository.existsByObraIdAndUsuarioId(obraId, usuario.getId());
            }
        }
        return CurtidaStatusDTO.builder()
                .total(curtidaRepository.countByObraId(obraId))
                .curtidoPeloUsuario(curtido)
                .build();
    }

    @Transactional
    public CurtidaStatusDTO curtir(UUID obraId, String emailUsuario) {
        Obra obra = obraRepository.findById(obraId)
                .orElseThrow(() -> new ObraNaoEncontradaException(obraId));
        Usuario usuario = usuarioRepository.findByEmail(emailUsuario)
                .orElseThrow(AcessoNegadoException::new);

        // Idempotente: só insere se ainda não existir
        if (!curtidaRepository.existsByObraIdAndUsuarioId(obraId, usuario.getId())) {
            curtidaRepository.save(Curtida.builder()
                    .obra(obra)
                    .usuario(usuario)
                    .build());
        }
        return montarStatus(obraId, true);
    }

    @Transactional
    public CurtidaStatusDTO descurtir(UUID obraId, String emailUsuario) {
        if (!obraRepository.existsById(obraId)) {
            throw new ObraNaoEncontradaException(obraId);
        }
        Usuario usuario = usuarioRepository.findByEmail(emailUsuario)
                .orElseThrow(AcessoNegadoException::new);

        curtidaRepository.deleteByObraIdAndUsuarioId(obraId, usuario.getId());
        return montarStatus(obraId, false);
    }

    private CurtidaStatusDTO montarStatus(UUID obraId, boolean curtido) {
        return CurtidaStatusDTO.builder()
                .total(curtidaRepository.countByObraId(obraId))
                .curtidoPeloUsuario(curtido)
                .build();
    }
}

package com.flowcarreiras.flowcarreiras_api.service;

import com.flowcarreiras.flowcarreiras_api.model.PerfilArtista;
import com.flowcarreiras.flowcarreiras_api.repository.PerfilArtistaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FilaDescobertaService {

    private final PerfilArtistaRepository perfilArtistaRepository;

    @Scheduled(cron = "0 30 3 * * *")
    @Transactional
    public void rotacionarFilaDescoberta() {
        LocalDateTime limite = LocalDateTime.now().minusHours(48);
        List<PerfilArtista> perfis = perfilArtistaRepository.listarParaRotacaoFila(limite);
        if (perfis.isEmpty()) return;
        LocalDateTime agora = LocalDateTime.now();
        for (PerfilArtista perfil : perfis) {
            perfil.setDataEntradaFila(agora);
        }
        perfilArtistaRepository.saveAll(perfis);
    }
}

package com.flowcarreiras.flowcarreiras_api.service;

import com.flowcarreiras.flowcarreiras_api.exception.ArquivoInvalidoException;
import com.flowcarreiras.flowcarreiras_api.model.enums.TipoMidia;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Service
public class FileStorageService {

    private static final long MAX_TAMANHO_IMAGEM = 10L * 1024 * 1024;  // 10 MB
    private static final long MAX_TAMANHO_AUDIO  = 30L * 1024 * 1024;  // 30 MB
    private static final long MAX_TAMANHO_VIDEO  = 30L * 1024 * 1024;  // 30 MB

    private static final Map<TipoMidia, Set<String>> CONTENT_TYPES_PERMITIDOS = Map.of(
        TipoMidia.IMAGEM, Set.of("image/jpeg", "image/png"),
        TipoMidia.AUDIO,  Set.of("audio/mpeg", "audio/wav", "audio/x-wav"),
        TipoMidia.VIDEO,  Set.of("video/mp4")
    );

    @Value("${storage.upload-dir}")
    private String uploadDir;

    public String salvar(MultipartFile file, TipoMidia tipoMidia) {
        validarArquivo(file, tipoMidia);

        try {
            Path pastaDestino = Paths.get(uploadDir)
                    .toAbsolutePath()
                    .resolve(tipoMidia.name().toLowerCase());
            Files.createDirectories(pastaDestino);

            String extensao = obterExtensao(file.getOriginalFilename());
            String nomeArquivo = UUID.randomUUID() + "." + extensao;
            Path destino = pastaDestino.resolve(nomeArquivo);

            Files.copy(file.getInputStream(), destino, StandardCopyOption.REPLACE_EXISTING);

            return "/uploads/" + tipoMidia.name().toLowerCase() + "/" + nomeArquivo;

        } catch (IOException e) {
            throw new ArquivoInvalidoException("Falha ao salvar arquivo. Tente novamente.");
        }
    }

    public void deletar(String urlMidia) {
        if (urlMidia == null || !urlMidia.startsWith("/uploads/")) return;
        try {
            Path arquivo = Paths.get(uploadDir).toAbsolutePath()
                    .resolve(urlMidia.substring("/uploads/".length()));
            Files.deleteIfExists(arquivo);
        } catch (IOException ignored) {
            // Falha silenciosa na remoção — não bloqueia o fluxo principal
        }
    }

    private void validarArquivo(MultipartFile file, TipoMidia tipoMidia) {
        if (file == null || file.isEmpty()) {
            throw new ArquivoInvalidoException("Arquivo não pode estar vazio");
        }

        String contentType = file.getContentType();
        Set<String> permitidos = CONTENT_TYPES_PERMITIDOS.get(tipoMidia);

        if (permitidos == null || !permitidos.contains(contentType)) {
            throw new ArquivoInvalidoException(
                "Formato não suportado: " + contentType +
                ". Permitidos: " + String.join(", ", permitidos != null ? permitidos : Set.of())
            );
        }

        long maxTamanho = switch (tipoMidia) {
            case IMAGEM -> MAX_TAMANHO_IMAGEM;
            case AUDIO  -> MAX_TAMANHO_AUDIO;
            case VIDEO  -> MAX_TAMANHO_VIDEO;
            default     -> MAX_TAMANHO_IMAGEM;
        };

        if (file.getSize() > maxTamanho) {
            throw new ArquivoInvalidoException(
                "Arquivo excede o tamanho máximo de " + (maxTamanho / (1024 * 1024)) + " MB"
            );
        }
    }

    private String obterExtensao(String nomeArquivo) {
        if (nomeArquivo == null || !nomeArquivo.contains(".")) return "bin";
        return nomeArquivo.substring(nomeArquivo.lastIndexOf('.') + 1).toLowerCase();
    }
}

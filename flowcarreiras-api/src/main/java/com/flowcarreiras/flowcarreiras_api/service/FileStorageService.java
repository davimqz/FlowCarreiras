package com.flowcarreiras.flowcarreiras_api.service;

import com.flowcarreiras.flowcarreiras_api.exception.ArquivoInvalidoException;
import com.flowcarreiras.flowcarreiras_api.model.enums.TipoMidia;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Service
public class FileStorageService {

    private static final long MAX_TAMANHO_IMAGEM      = 10L * 1024 * 1024;  // 10 MB
    private static final long MAX_TAMANHO_AUDIO       = 30L * 1024 * 1024;  // 30 MB
    private static final long MAX_TAMANHO_VIDEO       = 30L * 1024 * 1024;  // 30 MB
    private static final long MAX_TAMANHO_FOTO_PERFIL =  5L * 1024 * 1024;  //  5 MB
    private static final Set<String> CONTENT_TYPES_FOTO_PERFIL = Set.of("image/jpeg", "image/png");

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

    public String salvarFotoPerfil(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new ArquivoInvalidoException("Arquivo não pode estar vazio");
        }
        if (!CONTENT_TYPES_FOTO_PERFIL.contains(file.getContentType())) {
            throw new ArquivoInvalidoException("Apenas imagens JPG ou PNG são aceitas para foto de perfil");
        }
        if (file.getSize() > MAX_TAMANHO_FOTO_PERFIL) {
            throw new ArquivoInvalidoException("Foto de perfil deve ter no máximo 5 MB");
        }
        try {
            Path pastaDestino = Paths.get(uploadDir).toAbsolutePath().resolve("perfil");
            Files.createDirectories(pastaDestino);
            String extensao = obterExtensao(file.getOriginalFilename());
            String nomeArquivo = UUID.randomUUID() + "." + extensao;
            Files.copy(file.getInputStream(), pastaDestino.resolve(nomeArquivo), StandardCopyOption.REPLACE_EXISTING);
            return "/uploads/perfil/" + nomeArquivo;
        } catch (IOException e) {
            throw new ArquivoInvalidoException("Falha ao salvar foto de perfil. Tente novamente.");
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

    public long obterTamanhoArquivo(String urlMidia) {
        Path arquivo = resolverCaminho(urlMidia);
        if (arquivo == null) return 0L;
        try {
            return Files.size(arquivo);
        } catch (IOException e) {
            return 0L;
        }
    }

    public boolean validarCabecalhoMidia(MultipartFile file, TipoMidia tipoMidia) {
        if (file == null || file.isEmpty()) return false;
        try (InputStream inputStream = file.getInputStream()) {
            byte[] header = inputStream.readNBytes(16);
            return cabecalhoValido(header, tipoMidia);
        } catch (IOException e) {
            return false;
        }
    }

    public boolean validarCabecalhoMidia(String urlMidia, TipoMidia tipoMidia) {
        Path arquivo = resolverCaminho(urlMidia);
        if (arquivo == null) return false;
        try (InputStream inputStream = Files.newInputStream(arquivo)) {
            byte[] header = inputStream.readNBytes(16);
            return cabecalhoValido(header, tipoMidia);
        } catch (IOException e) {
            return false;
        }
    }

    private Path resolverCaminho(String urlMidia) {
        if (urlMidia == null || !urlMidia.startsWith("/uploads/")) return null;
        return Paths.get(uploadDir).toAbsolutePath()
                .resolve(urlMidia.substring("/uploads/".length()));
    }

    private boolean cabecalhoValido(byte[] header, TipoMidia tipoMidia) {
        if (header == null || header.length == 0) return false;
        return switch (tipoMidia) {
            case IMAGEM -> isJpeg(header) || isPng(header);
            case AUDIO -> isMp3(header) || isWav(header);
            case VIDEO -> isMp4(header);
            default -> false;
        };
    }

    private boolean isJpeg(byte[] header) {
        return header.length >= 3
                && (header[0] & 0xFF) == 0xFF
                && (header[1] & 0xFF) == 0xD8
                && (header[2] & 0xFF) == 0xFF;
    }

    private boolean isPng(byte[] header) {
        byte[] png = {(byte) 0x89, 0x50, 0x4E, 0x47, 0x0D, 0x0A, 0x1A, 0x0A};
        return header.length >= png.length && Arrays.equals(Arrays.copyOf(header, png.length), png);
    }

    private boolean isMp3(byte[] header) {
        if (header.length < 3) return false;
        boolean id3 = header[0] == 'I' && header[1] == 'D' && header[2] == '3';
        boolean frame = (header[0] & 0xFF) == 0xFF && (header[1] & 0xE0) == 0xE0;
        return id3 || frame;
    }

    private boolean isWav(byte[] header) {
        return header.length >= 12
                && header[0] == 'R' && header[1] == 'I' && header[2] == 'F' && header[3] == 'F'
                && header[8] == 'W' && header[9] == 'A' && header[10] == 'V' && header[11] == 'E';
    }

    private boolean isMp4(byte[] header) {
        return header.length >= 12
                && header[4] == 'f' && header[5] == 't' && header[6] == 'y' && header[7] == 'p';
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

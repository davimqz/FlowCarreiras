package com.flowcarreiras.flowcarreiras_api.service;

import com.flowcarreiras.flowcarreiras_api.dto.auth.AuthResponseDTO;
import com.flowcarreiras.flowcarreiras_api.dto.auth.LoginRequestDTO;
import com.flowcarreiras.flowcarreiras_api.dto.auth.RegistroRequestDTO;
import com.flowcarreiras.flowcarreiras_api.exception.EmailJaCadastradoException;
import com.flowcarreiras.flowcarreiras_api.model.PerfilArtista;
import com.flowcarreiras.flowcarreiras_api.model.Usuario;
import com.flowcarreiras.flowcarreiras_api.repository.PerfilArtistaRepository;
import com.flowcarreiras.flowcarreiras_api.repository.UsuarioRepository;
import com.flowcarreiras.flowcarreiras_api.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.Normalizer;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PerfilArtistaRepository perfilArtistaRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Transactional
    public AuthResponseDTO registrar(RegistroRequestDTO dto) {
        if (usuarioRepository.existsByEmail(dto.getEmail())) {
            throw new EmailJaCadastradoException(dto.getEmail());
        }

        Usuario usuario = Usuario.builder()
                .nome(dto.getNome())
                .email(dto.getEmail())
                .senha(passwordEncoder.encode(dto.getSenha()))
                .build();
        usuarioRepository.save(usuario);

        String urlPublica = gerarUrlPublica(dto.getNome());
        PerfilArtista perfil = PerfilArtista.builder()
                .usuario(usuario)
                .urlPublica(urlPublica)
                .build();
        perfilArtistaRepository.save(perfil);

        String token = gerarToken(usuario.getEmail());
        return AuthResponseDTO.builder()
                .token(token)
                .tipo("Bearer")
                .usuarioId(usuario.getId())
                .nome(usuario.getNome())
                .email(usuario.getEmail())
                .perfilArtistaId(perfil.getId())
                .urlPublica(urlPublica)
                .build();
    }

    public AuthResponseDTO login(LoginRequestDTO dto) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getSenha())
        );

        Usuario usuario = usuarioRepository.findByEmail(dto.getEmail()).orElseThrow();
        PerfilArtista perfil = perfilArtistaRepository.findByUsuarioEmail(dto.getEmail()).orElseThrow();

        String token = gerarToken(usuario.getEmail());
        return AuthResponseDTO.builder()
                .token(token)
                .tipo("Bearer")
                .usuarioId(usuario.getId())
                .nome(usuario.getNome())
                .email(usuario.getEmail())
                .perfilArtistaId(perfil.getId())
                .urlPublica(perfil.getUrlPublica())
                .build();
    }

    private String gerarToken(String email) {
        return jwtService.gerarToken(User.builder()
                .username(email)
                .password("")
                .roles("ARTISTA")
                .build());
    }

    // Gera slug URL-amigável: "Marina Alves" -> "marina-alves-a1b2c3d4"
    private String gerarUrlPublica(String nome) {
        String slug = Normalizer.normalize(nome, Normalizer.Form.NFD)
                .replaceAll("[^\\p{ASCII}]", "")
                .toLowerCase()
                .replaceAll("[^a-z0-9]+", "-")
                .replaceAll("^-|-$", "");

        String sufixo = UUID.randomUUID().toString().substring(0, 8);
        String candidato = slug + "-" + sufixo;

        // Garante unicidade (colisão improvável mas tratada)
        while (perfilArtistaRepository.existsByUrlPublica(candidato)) {
            candidato = slug + "-" + UUID.randomUUID().toString().substring(0, 8);
        }
        return candidato;
    }
}

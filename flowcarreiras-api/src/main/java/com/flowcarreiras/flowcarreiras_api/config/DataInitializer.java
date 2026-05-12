package com.flowcarreiras.flowcarreiras_api.config;

import com.flowcarreiras.flowcarreiras_api.model.PerfilArtista;
import com.flowcarreiras.flowcarreiras_api.model.Tag;
import com.flowcarreiras.flowcarreiras_api.model.Usuario;
import com.flowcarreiras.flowcarreiras_api.model.enums.CategoriaTag;
import com.flowcarreiras.flowcarreiras_api.repository.PerfilArtistaRepository;
import com.flowcarreiras.flowcarreiras_api.repository.TagRepository;
import com.flowcarreiras.flowcarreiras_api.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final TagRepository tagRepository;
    private final UsuarioRepository usuarioRepository;
    private final PerfilArtistaRepository perfilArtistaRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${seed.teste-senha:senha123}")
    private String senhaTeste;

    @Override
    @Transactional
    public void run(String... args) {
        seedTags();
        seedUsuarioTeste();
    }

    private void seedTags() {
        if (tagRepository.count() > 0) return;

        List<Tag> tags = List.of(
            // AREA_ARTISTICA
            tag("ilustração",      CategoriaTag.AREA_ARTISTICA),
            tag("música",          CategoriaTag.AREA_ARTISTICA),
            tag("fotografia",      CategoriaTag.AREA_ARTISTICA),
            tag("design gráfico",  CategoriaTag.AREA_ARTISTICA),
            tag("pintura",         CategoriaTag.AREA_ARTISTICA),
            tag("audiovisual",     CategoriaTag.AREA_ARTISTICA),
            tag("dança",           CategoriaTag.AREA_ARTISTICA),
            tag("teatro",          CategoriaTag.AREA_ARTISTICA),
            tag("escultura",       CategoriaTag.AREA_ARTISTICA),

            // HABILIDADE
            tag("edição de vídeo",    CategoriaTag.HABILIDADE),
            tag("composição musical", CategoriaTag.HABILIDADE),
            tag("animação 2D",        CategoriaTag.HABILIDADE),
            tag("lettering",          CategoriaTag.HABILIDADE),
            tag("direção de arte",    CategoriaTag.HABILIDADE),
            tag("motion graphics",    CategoriaTag.HABILIDADE),

            // ESTILO
            tag("minimalista",   CategoriaTag.ESTILO),
            tag("realismo",      CategoriaTag.ESTILO),
            tag("abstrato",      CategoriaTag.ESTILO),
            tag("experimental",  CategoriaTag.ESTILO),
            tag("pop art",       CategoriaTag.ESTILO),

            // FORMATO
            tag("retrato",    CategoriaTag.FORMATO),
            tag("paisagem",   CategoriaTag.FORMATO),
            tag("still life", CategoriaTag.FORMATO),
            tag("animação",   CategoriaTag.FORMATO),
            tag("série",      CategoriaTag.FORMATO)
        );

        tagRepository.saveAll(tags);
    }

    private void seedUsuarioTeste() {
        if (usuarioRepository.existsByEmail("marina@test.com")) return;

        Usuario marina = Usuario.builder()
                .nome("Marina Alves")
                .email("marina@test.com")
                .senha(passwordEncoder.encode(senhaTeste))
                .build();
        usuarioRepository.save(marina);

        PerfilArtista perfil = PerfilArtista.builder()
                .usuario(marina)
                .bio("Artista visual e ilustradora do Recife. Apaixonada por cor e narrativa.")
                .cidade("Recife, PE")
                .areaArtisticaPrincipal("ilustração")
                .urlPublica("marina-alves-teste")
                .onboardingConcluido(true)
                .build();
        perfilArtistaRepository.save(perfil);
    }

    private Tag tag(String nome, CategoriaTag categoria) {
        return Tag.builder().nome(nome).categoria(categoria).build();
    }
}

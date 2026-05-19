package com.flowcarreiras.flowcarreiras_api.config;

import com.flowcarreiras.flowcarreiras_api.model.PerfilArtista;
import com.flowcarreiras.flowcarreiras_api.model.Tag;
import com.flowcarreiras.flowcarreiras_api.model.Usuario;
import com.flowcarreiras.flowcarreiras_api.model.enums.CategoriaTag;
import com.flowcarreiras.flowcarreiras_api.model.enums.ModalidadeMentoria;
import com.flowcarreiras.flowcarreiras_api.repository.PerfilArtistaRepository;
import com.flowcarreiras.flowcarreiras_api.repository.TagRepository;
import com.flowcarreiras.flowcarreiras_api.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
        seedUsuariosTeste();
    }

    private void seedTags() {
        if (tagRepository.count() > 0) return;

        List<Tag> tags = List.of(
            tag("ilustracao", CategoriaTag.AREA_ARTISTICA),
            tag("musica", CategoriaTag.AREA_ARTISTICA),
            tag("fotografia", CategoriaTag.AREA_ARTISTICA),
            tag("design grafico", CategoriaTag.AREA_ARTISTICA),
            tag("pintura", CategoriaTag.AREA_ARTISTICA),
            tag("audiovisual", CategoriaTag.AREA_ARTISTICA),
            tag("danca", CategoriaTag.AREA_ARTISTICA),
            tag("teatro", CategoriaTag.AREA_ARTISTICA),
            tag("escultura", CategoriaTag.AREA_ARTISTICA),
            tag("edicao de video", CategoriaTag.HABILIDADE),
            tag("composicao musical", CategoriaTag.HABILIDADE),
            tag("animacao 2D", CategoriaTag.HABILIDADE),
            tag("lettering", CategoriaTag.HABILIDADE),
            tag("direcao de arte", CategoriaTag.HABILIDADE),
            tag("motion graphics", CategoriaTag.HABILIDADE),
            tag("minimalista", CategoriaTag.ESTILO),
            tag("realismo", CategoriaTag.ESTILO),
            tag("abstrato", CategoriaTag.ESTILO),
            tag("experimental", CategoriaTag.ESTILO),
            tag("pop art", CategoriaTag.ESTILO),
            tag("retrato", CategoriaTag.FORMATO),
            tag("paisagem", CategoriaTag.FORMATO),
            tag("still life", CategoriaTag.FORMATO),
            tag("animacao", CategoriaTag.FORMATO),
            tag("serie", CategoriaTag.FORMATO)
        );

        tagRepository.saveAll(tags);
    }

    private void seedUsuariosTeste() {
        List<Tag> tags = tagRepository.findAll();

        PerfilArtista marina = criarPerfilSeAusente(
                "Marina Alves",
                "marina@test.com",
                "Artista visual e ilustradora do Recife. Ajuda artistas a construir portfolio e narrativa visual.",
                "Recife, PE",
                "Ilustracao",
                "marina-alves-teste",
                LocalDateTime.now().minusDays(8));
        marina.setPerfilMentorConfigurado(true);
        marina.setDisponivelParaMentorar(true);
        marina.setMentoriaGratuita(true);
        marina.setModalidadeMentoria(ModalidadeMentoria.HIBRIDA);
        marina.setCidadeMentoria("Recife, PE");
        marina.setDescricaoMentoria("Mentoria para portfolio, identidade visual e apresentacao de projetos artisticos.");
        marina.setTagsExpertise(tagsPorNome(tags, "fotografia", "lettering", "minimalista"));
        marina.setTagsNecessidade(tagsPorNome(tags, "motion graphics"));
        marina.setPercentualCompletude(marina.calcularPercentualCompletude());
        perfilArtistaRepository.save(marina);

        PerfilArtista bruno = criarPerfilSeAusente(
                "Bruno Costa",
                "bruno@test.com",
                "Diretor audiovisual com experiencia em videoclipes, documentario curto e processos de producao.",
                "Joao Pessoa, PB",
                "Audiovisual",
                "bruno-costa-teste",
                LocalDateTime.now().minusDays(7));
        bruno.setPerfilMentorConfigurado(true);
        bruno.setDisponivelParaMentorar(true);
        bruno.setMentoriaGratuita(false);
        bruno.setValorHoraMentoria(new BigDecimal("120.00"));
        bruno.setModalidadeMentoria(ModalidadeMentoria.REMOTA);
        bruno.setCidadeMentoria("Joao Pessoa, PB");
        bruno.setDescricaoMentoria("Mentoria remota para planejamento audiovisual, edicao e distribuicao de trabalhos independentes.");
        bruno.setTagsExpertise(tagsPorNome(tags, "audiovisual", "motion graphics", "fotografia"));
        bruno.setTagsNecessidade(tagsPorNome(tags, "lettering"));
        bruno.setPercentualCompletude(bruno.calcularPercentualCompletude());
        perfilArtistaRepository.save(bruno);

        PerfilArtista ana = criarPerfilSeAusente(
                "Ana Ribeiro",
                "ana@test.com",
                "Fotografa em inicio de carreira pesquisando cor, retrato e divulgacao de portfolio.",
                "Joao Pessoa, PB",
                "Fotografia",
                "ana-ribeiro-teste",
                LocalDateTime.now().minusDays(6));
        ana.setTagsNecessidade(tagsPorNome(tags, "fotografia", "motion graphics", "pop art"));
        ana.setPercentualCompletude(ana.calcularPercentualCompletude());
        perfilArtistaRepository.save(ana);

        PerfilArtista rafa = criarPerfilSeAusente(
                "Rafa Lima",
                "rafa@test.com",
                "Designer e artista independente buscando amadurecer linguagem visual e precificacao.",
                "Recife, PE",
                "Design grafico",
                "rafa-lima-teste",
                LocalDateTime.now().minusDays(5));
        rafa.setTagsNecessidade(tagsPorNome(tags, "lettering", "minimalista", "retrato"));
        rafa.setPercentualCompletude(rafa.calcularPercentualCompletude());
        perfilArtistaRepository.save(rafa);

        PerfilArtista dandara = criarPerfilSeAusente(
                "Dandara Melo",
                "dandara@test.com",
                "Performer e artista multimidia interessada em audiovisual experimental.",
                "Olinda, PE",
                "Performance",
                "dandara-melo-teste",
                LocalDateTime.now().minusDays(4));
        dandara.setTagsNecessidade(tagsPorNome(tags, "audiovisual", "experimental", "fotografia"));
        dandara.setPercentualCompletude(dandara.calcularPercentualCompletude());
        perfilArtistaRepository.save(dandara);
    }

    private Tag tag(String nome, CategoriaTag categoria) {
        return Tag.builder().nome(nome).categoria(categoria).build();
    }

    private PerfilArtista criarPerfilSeAusente(
            String nome,
            String email,
            String bio,
            String cidade,
            String area,
            String urlPublica,
            LocalDateTime dataEntradaFila) {
        return perfilArtistaRepository.findByUsuarioEmail(email)
                .orElseGet(() -> {
                    Usuario usuario = usuarioRepository.findByEmail(email)
                            .orElseGet(() -> usuarioRepository.save(Usuario.builder()
                                    .nome(nome)
                                    .email(email)
                                    .senha(passwordEncoder.encode(senhaTeste))
                                    .build()));

                    return perfilArtistaRepository.save(PerfilArtista.builder()
                            .usuario(usuario)
                            .bio(bio)
                            .cidade(cidade)
                            .areaArtisticaPrincipal(area)
                            .urlPublica(urlPublica)
                            .dataEntradaFila(dataEntradaFila)
                            .onboardingConcluido(true)
                            .build());
                });
    }

    private Set<Tag> tagsPorNome(List<Tag> todas, String... nomes) {
        Set<Tag> resultado = new HashSet<>();
        for (String nome : nomes) {
            todas.stream()
                    .filter(tag -> tag.getNome().equalsIgnoreCase(nome))
                    .findFirst()
                    .ifPresent(resultado::add);
        }
        return resultado;
    }
}

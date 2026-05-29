package com.flowcarreiras.flowcarreiras_api.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.flowcarreiras.flowcarreiras_api.config.OportunidadesConfig;
import com.flowcarreiras.flowcarreiras_api.dto.oportunidades.OportunidadeResponseDTO;
import com.flowcarreiras.flowcarreiras_api.model.enums.OportunidadeFonteFormato;
import com.flowcarreiras.flowcarreiras_api.model.enums.OportunidadeTipo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilderFactory;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OportunidadeService {

    private final OportunidadesConfig config;
    private final ObjectMapper objectMapper;
    private final RestTemplate restTemplate = new RestTemplate();

    private volatile Instant lastFetch = Instant.EPOCH;
    private volatile List<OportunidadeResponseDTO> cache = new ArrayList<>();

    public List<OportunidadeResponseDTO> listar(Filtro filtro) {
        atualizarCacheSeNecessario();
        return aplicarFiltro(cache, filtro);
    }

    private void atualizarCacheSeNecessario() {
        int ttl = Math.max(config.getTtlSeconds(), 60);
        if (Instant.now().isBefore(lastFetch.plusSeconds(ttl))) {
            return;
        }
        List<OportunidadeResponseDTO> novas = new ArrayList<>();
        for (OportunidadesConfig.FonteConfig fonte : config.getFontes()) {
            novas.addAll(buscarFonte(fonte));
        }
        cache = novas;
        lastFetch = Instant.now();
    }

    private List<OportunidadeResponseDTO> buscarFonte(OportunidadesConfig.FonteConfig fonte) {
        if (!StringUtils.hasText(fonte.getUrl())) {
            return List.of();
        }
        OportunidadeFonteFormato formato = parseFormato(fonte.getFormato());
        String payload = restTemplate.getForObject(fonte.getUrl(), String.class);
        if (!StringUtils.hasText(payload)) {
            return List.of();
        }
        return formato == OportunidadeFonteFormato.RSS
                ? parseRss(payload, fonte)
                : parseJson(payload, fonte);
    }

    private List<OportunidadeResponseDTO> parseRss(String xml, OportunidadesConfig.FonteConfig fonte) {
        List<OportunidadeResponseDTO> out = new ArrayList<>();
        try {
            var doc = DocumentBuilderFactory.newInstance().newDocumentBuilder()
                    .parse(new java.io.ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8)));
            NodeList itens = doc.getElementsByTagName("item");
            for (int i = 0; i < itens.getLength(); i++) {
                Element item = (Element) itens.item(i);
                String titulo = getText(item, "title");
                String link = getText(item, "link");
                String descricao = getText(item, "description");
                String data = getText(item, "pubDate");
                List<String> tags = new ArrayList<>(fonte.getTags());
                NodeList categorias = item.getElementsByTagName("category");
                for (int c = 0; c < categorias.getLength(); c++) {
                    String cat = categorias.item(c).getTextContent();
                    if (StringUtils.hasText(cat)) tags.add(cat.trim());
                }
                out.add(buildOportunidade(fonte, titulo, descricao, data, link, tags));
            }
        } catch (Exception ignored) {
            return List.of();
        }
        return out;
    }

    private List<OportunidadeResponseDTO> parseJson(String json, OportunidadesConfig.FonteConfig fonte) {
        List<OportunidadeResponseDTO> out = new ArrayList<>();
        try {
            JsonNode root = objectMapper.readTree(json);
            JsonNode items = root.has("items") ? root.get("items") : root;
            if (items.isArray()) {
                for (JsonNode item : items) {
                    String titulo = getText(item, "title", "titulo", "name");
                    String link = getText(item, "link", "url");
                    String descricao = getText(item, "description", "descricao", "summary");
                    String data = getText(item, "date", "data", "publishedAt");
                    List<String> tags = new ArrayList<>(fonte.getTags());
                    JsonNode tagsNode = item.get("tags");
                    if (tagsNode != null && tagsNode.isArray()) {
                        for (JsonNode t : tagsNode) {
                            if (t.isTextual()) tags.add(t.asText());
                        }
                    }
                    out.add(buildOportunidade(fonte, titulo, descricao, data, link, tags));
                }
            }
        } catch (Exception ignored) {
            return List.of();
        }
        return out;
    }

    private OportunidadeResponseDTO buildOportunidade(
            OportunidadesConfig.FonteConfig fonte,
            String titulo,
            String descricao,
            String data,
            String link,
            List<String> tags
    ) {
        String base = (titulo == null ? "" : titulo) + "|" + (link == null ? "" : link);
        String id = UUID.nameUUIDFromBytes(base.getBytes(StandardCharsets.UTF_8)).toString();
        String tipo = parseTipo(fonte.getTipo()).name();
        return OportunidadeResponseDTO.builder()
                .id(id)
                .titulo(titulo)
                .descricao(descricao)
                .tipo(tipo)
                .data(data)
                .localidade(fonte.getLocalidade())
                .link(link)
                .areaArtistica(fonte.getAreaArtistica())
                .fonte(fonte.getFonte())
                .tags(tags)
                .build();
    }

    private List<OportunidadeResponseDTO> aplicarFiltro(List<OportunidadeResponseDTO> entradas, Filtro filtro) {
        if (entradas.isEmpty()) return List.of();
        List<OportunidadeResponseDTO> out = new ArrayList<>();
        for (OportunidadeResponseDTO o : entradas) {
            if (!filtro.tipos().isEmpty() && !filtro.tipos().contains(o.getTipo())) continue;
            if (!filtro.tags().isEmpty() && !matchTags(o.getTags(), filtro.tags())) continue;
            if (StringUtils.hasText(filtro.area()) && !containsIgnoreCase(o.getAreaArtistica(), filtro.area())) continue;
            if (StringUtils.hasText(filtro.localidade()) && !containsIgnoreCase(o.getLocalidade(), filtro.localidade())) continue;
            if (StringUtils.hasText(filtro.query()) && !matchQuery(o, filtro.query())) continue;
            out.add(o);
        }
        int start = Math.min(filtro.offset(), out.size());
        int end = Math.min(start + filtro.limit(), out.size());
        return out.subList(start, end);
    }

    private boolean matchTags(List<String> tags, Set<String> filtro) {
        if (tags == null || tags.isEmpty()) return false;
        for (String tag : tags) {
            if (tag == null) continue;
            if (filtro.contains(tag.toLowerCase(Locale.ROOT))) return true;
        }
        return false;
    }

    private boolean matchQuery(OportunidadeResponseDTO o, String query) {
        String q = query.toLowerCase(Locale.ROOT);
        return containsIgnoreCase(o.getTitulo(), q) || containsIgnoreCase(o.getDescricao(), q);
    }

    private boolean containsIgnoreCase(String valor, String busca) {
        if (!StringUtils.hasText(valor)) return false;
        return valor.toLowerCase(Locale.ROOT).contains(busca.toLowerCase(Locale.ROOT));
    }

    private String getText(Element item, String tag) {
        NodeList nodes = item.getElementsByTagName(tag);
        if (nodes.getLength() == 0) return null;
        return nodes.item(0).getTextContent();
    }

    private String getText(JsonNode node, String... fields) {
        for (String f : fields) {
            JsonNode v = node.get(f);
            if (v != null && v.isTextual()) return v.asText();
        }
        return null;
    }

    private OportunidadeFonteFormato parseFormato(String valor) {
        if (!StringUtils.hasText(valor)) return OportunidadeFonteFormato.RSS;
        try {
            return OportunidadeFonteFormato.valueOf(valor.toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException ex) {
            return OportunidadeFonteFormato.RSS;
        }
    }

    private OportunidadeTipo parseTipo(String valor) {
        if (!StringUtils.hasText(valor)) return OportunidadeTipo.OPORTUNIDADE;
        try {
            return OportunidadeTipo.valueOf(valor.toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException ex) {
            return OportunidadeTipo.OPORTUNIDADE;
        }
    }

    public record Filtro(Set<String> tipos, Set<String> tags, String area, String localidade, String query, int limit, int offset) {
        public static Filtro vazio() {
            return new Filtro(Set.of(), Set.of(), null, null, null, 50, 0);
        }
    }
}
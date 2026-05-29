package com.flowcarreiras.flowcarreiras_api.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Data
@Component
@ConfigurationProperties(prefix = "oportunidades")
public class OportunidadesConfig {

    private int ttlSeconds = 600;
    private List<FonteConfig> fontes = new ArrayList<>();

    @Data
    public static class FonteConfig {
        private String id;
        private String url;
        private String fonte;
        private String tipo;
        private String formato;
        private String areaArtistica;
        private String localidade;
        private List<String> tags = new ArrayList<>();
    }
}

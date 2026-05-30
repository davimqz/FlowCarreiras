CREATE TABLE IF NOT EXISTS oportunidades (
    id UUID PRIMARY KEY,
    titulo VARCHAR(200) NOT NULL,
    descricao VARCHAR(2000),
    tipo VARCHAR(40) NOT NULL,
    area_artistica VARCHAR(120) NOT NULL,
    data_encerramento DATE NOT NULL,
    link_externo VARCHAR(500) NOT NULL,
    status VARCHAR(20) NOT NULL,
    data_criacao TIMESTAMP NOT NULL
);

CREATE TABLE IF NOT EXISTS oportunidade_tags (
    oportunidade_id UUID NOT NULL,
    tag_id UUID NOT NULL,
    PRIMARY KEY (oportunidade_id, tag_id),
    CONSTRAINT fk_oportunidade_tags_oportunidade FOREIGN KEY (oportunidade_id) REFERENCES oportunidades(id),
    CONSTRAINT fk_oportunidade_tags_tag FOREIGN KEY (tag_id) REFERENCES tags(id)
);

CREATE INDEX IF NOT EXISTS idx_oportunidades_status_data ON oportunidades(status, data_encerramento);
CREATE INDEX IF NOT EXISTS idx_oportunidade_tags_tag ON oportunidade_tags(tag_id);

ALTER TABLE perfis_artistas
ADD COLUMN IF NOT EXISTS receber_notificacoes_oportunidades BOOLEAN NOT NULL DEFAULT TRUE;

CREATE TABLE IF NOT EXISTS notificacoes_oportunidades (
    id UUID PRIMARY KEY,
    perfil_id UUID NOT NULL,
    oportunidade_id UUID NOT NULL,
    titulo VARCHAR(200) NOT NULL,
    tipo VARCHAR(40) NOT NULL,
    data_encerramento DATE NOT NULL,
    lida BOOLEAN NOT NULL DEFAULT FALSE,
    data_criacao TIMESTAMP NOT NULL,
    CONSTRAINT fk_notif_perfil FOREIGN KEY (perfil_id) REFERENCES perfis_artistas(id),
    CONSTRAINT fk_notif_oportunidade FOREIGN KEY (oportunidade_id) REFERENCES oportunidades(id)
);

CREATE INDEX IF NOT EXISTS idx_notif_perfil_data ON notificacoes_oportunidades(perfil_id, data_criacao);
CREATE INDEX IF NOT EXISTS idx_notif_perfil_lida ON notificacoes_oportunidades(perfil_id, lida);

CREATE TABLE IF NOT EXISTS fila_descoberta_log (
    id UUID PRIMARY KEY,
    perfil_id UUID NOT NULL,
    obra_id UUID NOT NULL,
    data_hora TIMESTAMP NOT NULL,
    CONSTRAINT fk_fila_log_perfil FOREIGN KEY (perfil_id) REFERENCES perfis_artistas(id),
    CONSTRAINT fk_fila_log_obra FOREIGN KEY (obra_id) REFERENCES obras(id)
);

CREATE INDEX IF NOT EXISTS idx_fila_log_perfil ON fila_descoberta_log(perfil_id, data_hora);

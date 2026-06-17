CREATE TABLE IF NOT EXISTS seguidores (
    id UUID PRIMARY KEY,
    seguidor_id UUID NOT NULL,
    seguido_id UUID NOT NULL,
    data_criacao TIMESTAMP NOT NULL,
    CONSTRAINT fk_seguidor_origem FOREIGN KEY (seguidor_id) REFERENCES usuarios(id) ON DELETE CASCADE,
    CONSTRAINT fk_seguidor_alvo FOREIGN KEY (seguido_id) REFERENCES usuarios(id) ON DELETE CASCADE,
    CONSTRAINT uk_seguidor_seguido UNIQUE (seguidor_id, seguido_id)
);

CREATE INDEX IF NOT EXISTS idx_seguidor_seguido ON seguidores(seguido_id);
CREATE INDEX IF NOT EXISTS idx_seguidor_seguidor ON seguidores(seguidor_id);

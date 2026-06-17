CREATE TABLE IF NOT EXISTS curtidas (
    id UUID PRIMARY KEY,
    obra_id UUID NOT NULL,
    usuario_id UUID NOT NULL,
    data_criacao TIMESTAMP NOT NULL,
    CONSTRAINT fk_curtida_obra FOREIGN KEY (obra_id) REFERENCES obras(id) ON DELETE CASCADE,
    CONSTRAINT fk_curtida_usuario FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE,
    CONSTRAINT uk_curtida_obra_usuario UNIQUE (obra_id, usuario_id)
);

CREATE INDEX IF NOT EXISTS idx_curtida_obra ON curtidas(obra_id);

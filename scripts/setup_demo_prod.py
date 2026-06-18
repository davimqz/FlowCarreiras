"""Popula uma instancia remota do FlowCarreiras uma unica vez."""

from __future__ import annotations

import os
import subprocess
import sys
import time
from pathlib import Path

import psycopg2


SCRIPT_DIR = Path(__file__).resolve().parent
TIAGO_EMAIL = "tiago@test.com"


def conectar():
    return psycopg2.connect(
        host=os.getenv("DB_HOST", "db"),
        port=os.getenv("DB_PORT", "5432"),
        dbname=os.getenv("DB_NAME", "flowcarreiras"),
        user=os.getenv("DB_USER", "postgres"),
        password=os.getenv("DB_PASSWORD", "postgres"),
    )


def aguardar_seed_base(tentativas: int = 240, intervalo: int = 5) -> None:
    for tentativa in range(1, tentativas + 1):
        try:
            with conectar() as conn, conn.cursor() as cur:
                cur.execute(
                    """
                    SELECT EXISTS (
                        SELECT 1 FROM information_schema.tables
                        WHERE table_schema = 'public' AND table_name = 'usuarios'
                    )
                    """
                )
                tabela_existe = cur.fetchone()[0]
                if tabela_existe:
                    cur.execute("SELECT EXISTS (SELECT 1 FROM usuarios WHERE email = 'ana@test.com')")
                    if cur.fetchone()[0]:
                        print("Seed base do backend encontrado.")
                        return
        except psycopg2.Error as erro:
            print(f"Aguardando PostgreSQL/backend ({tentativa}/{tentativas}): {erro}")

        time.sleep(intervalo)

    raise RuntimeError("O backend nao criou o schema e os usuarios base dentro do prazo.")


def demo_ja_configurada() -> bool:
    with conectar() as conn, conn.cursor() as cur:
        cur.execute("SELECT EXISTS (SELECT 1 FROM usuarios WHERE email = %s)", (TIAGO_EMAIL,))
        return cur.fetchone()[0]


def executar_script(nome: str) -> None:
    caminho = SCRIPT_DIR / nome
    subprocess.run([sys.executable, str(caminho)], check=True, env=os.environ.copy())


def configurar_tiago() -> None:
    sql = """
    BEGIN;

    UPDATE perfis_artistas p SET
      disponivel_para_mentorar = true,
      perfil_mentor_configurado = true,
      mentoria_gratuita = false,
      valor_hora_mentoria = 120.00,
      modalidade_mentoria = 'HIBRIDA',
      cidade_mentoria = COALESCE(p.cidade, 'Olinda'),
      descricao_mentoria = 'Mentoria em construcao de portfolio, carreira artistica e estrategias de divulgacao para artistas emergentes.'
    FROM usuarios u
    WHERE u.id = p.usuario_id
      AND u.email = 'tiago-silva.243@sim.flowcarreiras.dev';

    WITH t AS (
      SELECT p.id FROM perfis_artistas p
      JOIN usuarios u ON u.id = p.usuario_id
      WHERE u.email = 'tiago-silva.243@sim.flowcarreiras.dev'
    ),
    mentor AS (
      SELECT id FROM perfis_artistas
      WHERE perfil_mentor_configurado = true
        AND id <> (SELECT id FROM t)
      LIMIT 1
    ),
    mentee AS (
      SELECT id FROM perfis_artistas
      WHERE id NOT IN ((SELECT id FROM t), (SELECT id FROM mentor))
      LIMIT 1
    )
    INSERT INTO mentorias (id, mentor_id, artista_id, status, data_criacao)
    SELECT gen_random_uuid(), (SELECT id FROM t), (SELECT id FROM mentee), 'ATIVA', now()
    UNION ALL
    SELECT gen_random_uuid(), (SELECT id FROM mentor), (SELECT id FROM t), 'ATIVA', now();

    WITH t AS (
      SELECT p.id FROM perfis_artistas p
      JOIN usuarios u ON u.id = p.usuario_id
      WHERE u.email = 'tiago-silva.243@sim.flowcarreiras.dev'
    )
    INSERT INTO obras (id, titulo, descricao, tipo_midia, url_midia, data_publicacao, status, artista_id)
    SELECT gen_random_uuid(), v.titulo, 'Rascunho em andamento.', v.tipo,
           CASE WHEN v.tipo = 'IMAGEM'
                THEN 'https://picsum.photos/seed/' || gen_random_uuid()::text || '/600/600'
                ELSE '/uploads/sim/' || gen_random_uuid()::text || '.bin'
           END,
           now(), 'RASCUNHO', (SELECT id FROM t)
    FROM (VALUES
      ('Ensaio inacabado', 'IMAGEM'),
      ('Esboco noturno', 'IMAGEM'),
      ('Demo instrumental', 'AUDIO'),
      ('Videoarte (corte bruto)', 'VIDEO'),
      ('Serie urbana - WIP', 'IMAGEM')
    ) AS v(titulo, tipo);

    UPDATE perfis_artistas
    SET foto_perfil = 'https://i.pravatar.cc/300?u=' || usuario_id::text
    WHERE disponivel_para_mentorar = true;

    UPDATE usuarios SET email = 'tiago@test.com'
    WHERE email = 'tiago-silva.243@sim.flowcarreiras.dev'
      AND NOT EXISTS (SELECT 1 FROM usuarios WHERE email = 'tiago@test.com');

    COMMIT;
    """

    with conectar() as conn, conn.cursor() as cur:
        cur.execute(sql)


def main() -> None:
    aguardar_seed_base()

    if demo_ja_configurada():
        print("Dataset de demonstracao ja configurado; nenhuma alteracao necessaria.")
        return

    executar_script("seed_simulado.py")
    executar_script("seed_comentarios.py")
    configurar_tiago()
    print("Deploy populado. Login: tiago@test.com / senha123")


if __name__ == "__main__":
    main()

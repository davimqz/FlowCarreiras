"""Cria variaveis derivadas para as duas bases tratadas."""

from __future__ import annotations

import argparse
from pathlib import Path

import numpy as np
import pandas as pd


PROJECT_DIR = Path(__file__).resolve().parents[2]
DEFAULT_MAPA_INPUT = PROJECT_DIR / "data" / "processed" / "mapa_cultural_pe_agentes.csv"
DEFAULT_CONTEMPART_INPUT = PROJECT_DIR / "data" / "processed" / "contempart_artists.csv"
DEFAULT_MAPA_OUTPUT = PROJECT_DIR / "data" / "processed" / "mapa_cultural_pe_agentes_enriquecido.csv"
DEFAULT_CONTEMPART_OUTPUT = PROJECT_DIR / "data" / "processed" / "contempart_artists_enriquecido.csv"


def preenchido(serie: pd.Series) -> pd.Series:
    return serie.fillna("").astype(str).str.strip().ne("")


def contar_lista(serie: pd.Series) -> pd.Series:
    return (
        serie.fillna("")
        .astype(str)
        .map(lambda valor: len([item for item in valor.split("|") if item.strip()]))
    )


def enriquecer_mapa(dataframe: pd.DataFrame) -> pd.DataFrame:
    resultado = dataframe.copy()
    resultado["data_criacao"] = pd.to_datetime(resultado["data_criacao"], errors="coerce")
    resultado["data_atualizacao"] = pd.to_datetime(resultado["data_atualizacao"], errors="coerce")
    resultado["possui_descricao"] = preenchido(resultado["descricao_curta"])
    resultado["possui_tags"] = preenchido(resultado["termos_tags"])
    resultado["possui_funcoes"] = preenchido(resultado["termos_funcoes"])
    resultado["possui_subareas"] = preenchido(resultado["termos_subareas"])
    resultado["quantidade_areas"] = contar_lista(resultado["termos_areas"])
    resultado["perfil_multidisciplinar"] = resultado["quantidade_areas"] > 1
    resultado["perfil_minimamente_estruturado"] = (
        resultado["possui_descricao"]
        & preenchido(resultado["termos_areas"])
        & (resultado["possui_tags"] | resultado["possui_funcoes"])
    )
    resultado["ano_criacao"] = resultado["data_criacao"].dt.year.astype("Int64")
    resultado["atualizacao_posterior"] = (
        resultado["data_criacao"].notna()
        & resultado["data_atualizacao"].notna()
        & (resultado["data_atualizacao"] > resultado["data_criacao"])
    )
    return resultado


def enriquecer_contempart(dataframe: pd.DataFrame) -> pd.DataFrame:
    resultado = dataframe.copy()
    resultado["possui_instagram"] = preenchido(resultado["instagram_handle"])
    resultado["possui_website"] = preenchido(resultado["website"])
    resultado["somente_instagram_informado"] = (
        resultado["possui_instagram"] & ~resultado["possui_website"]
    )
    resultado["sem_presenca_digital_informada"] = (
        ~resultado["possui_instagram"] & ~resultado["possui_website"]
    )

    seguidores = pd.to_numeric(resultado["follower_count"], errors="coerce")
    likes = pd.to_numeric(resultado["avg_likes"], errors="coerce")
    comentarios = pd.to_numeric(resultado["avg_comments"], errors="coerce")
    engajamento_calculavel = seguidores.gt(0) & likes.notna() & comentarios.notna()
    resultado["taxa_engajamento"] = np.where(
        engajamento_calculavel,
        ((likes + comentarios) / seguidores) * 100,
        np.nan,
    )
    resultado["metricas_engajamento_disponiveis"] = engajamento_calculavel

    validos = seguidores.dropna()
    limite_baixo = validos.quantile(0.33)
    limite_alto = validos.quantile(0.67)
    resultado["nivel_visibilidade"] = pd.cut(
        seguidores,
        bins=[-np.inf, limite_baixo, limite_alto, np.inf],
        labels=["baixo", "medio", "alto"],
        include_lowest=True,
    )

    mediana_imagens = pd.to_numeric(resultado["img_count"], errors="coerce").median()
    mediana_seguidores = seguidores.median()
    volume_alto = pd.to_numeric(resultado["img_count"], errors="coerce") >= mediana_imagens
    visibilidade_alta = seguidores >= mediana_seguidores
    resultado["quadrante_imagens_visibilidade"] = np.select(
        [
            volume_alto & visibilidade_alta,
            volume_alto & ~visibilidade_alta & seguidores.notna(),
            ~volume_alto & visibilidade_alta,
            ~volume_alto & ~visibilidade_alta & seguidores.notna(),
        ],
        [
            "volume_alto_visibilidade_alta",
            "volume_alto_visibilidade_baixa",
            "volume_baixo_visibilidade_alta",
            "volume_baixo_visibilidade_baixa",
        ],
        default=None,
    )
    return resultado


def main() -> None:
    parser = argparse.ArgumentParser()
    parser.add_argument("--mapa-input", type=Path, default=DEFAULT_MAPA_INPUT)
    parser.add_argument("--contempart-input", type=Path, default=DEFAULT_CONTEMPART_INPUT)
    parser.add_argument("--mapa-output", type=Path, default=DEFAULT_MAPA_OUTPUT)
    parser.add_argument("--contempart-output", type=Path, default=DEFAULT_CONTEMPART_OUTPUT)
    args = parser.parse_args()

    mapa = enriquecer_mapa(pd.read_csv(args.mapa_input))
    contempart = enriquecer_contempart(pd.read_csv(args.contempart_input))

    args.mapa_output.parent.mkdir(parents=True, exist_ok=True)
    mapa.to_csv(args.mapa_output, index=False, encoding="utf-8", date_format="%Y-%m-%d %H:%M:%S")
    contempart.to_csv(args.contempart_output, index=False, encoding="utf-8")

    print(f"Mapa Cultural enriquecido: {args.mapa_output}")
    print(f"contempArt enriquecido: {args.contempart_output}")


if __name__ == "__main__":
    main()

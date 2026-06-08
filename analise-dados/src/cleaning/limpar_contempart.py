"""Padroniza o CSV de artistas do contempArt sem alterar seu significado."""

from __future__ import annotations

import argparse
from pathlib import Path

import pandas as pd


PROJECT_DIR = Path(__file__).resolve().parents[2]
DEFAULT_INPUT = PROJECT_DIR / "data" / "raw" / "contempart_artists.csv"
DEFAULT_OUTPUT = PROJECT_DIR / "data" / "processed" / "contempart_artists.csv"


def main() -> None:
    parser = argparse.ArgumentParser()
    parser.add_argument("--input", type=Path, default=DEFAULT_INPUT)
    parser.add_argument("--output", type=Path, default=DEFAULT_OUTPUT)
    args = parser.parse_args()

    dataframe = pd.read_csv(args.input)
    colunas_texto = dataframe.select_dtypes(include=["object", "string"]).columns
    for coluna in colunas_texto:
        dataframe[coluna] = dataframe[coluna].map(
            lambda valor: valor.strip() if isinstance(valor, str) else valor
        )

    dataframe = dataframe.drop_duplicates(subset=["artist_id"], keep="last")
    dataframe = dataframe.sort_values("artist_id", kind="stable").reset_index(drop=True)

    args.output.parent.mkdir(parents=True, exist_ok=True)
    dataframe.to_csv(args.output, index=False, encoding="utf-8")
    print(f"{len(dataframe)} registros limpos salvos em {args.output}")


if __name__ == "__main__":
    main()

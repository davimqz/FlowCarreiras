"""Baixa o CSV de artistas do repositorio oficial do contempArt."""

from __future__ import annotations

import argparse
from pathlib import Path
from urllib.request import Request, urlopen


SOURCE_URL = (
    "https://raw.githubusercontent.com/georgeblck/contempart/"
    "main/data/artists.csv"
)
PROJECT_DIR = Path(__file__).resolve().parents[2]
DEFAULT_OUTPUT = PROJECT_DIR / "data" / "raw" / "contempart_artists.csv"


def main() -> None:
    parser = argparse.ArgumentParser()
    parser.add_argument("--output", type=Path, default=DEFAULT_OUTPUT)
    args = parser.parse_args()

    request = Request(
        SOURCE_URL,
        headers={"User-Agent": "FlowCarreiras-AnaliseDados/1.0"},
    )
    with urlopen(request, timeout=60) as response:
        conteudo = response.read()

    args.output.parent.mkdir(parents=True, exist_ok=True)
    args.output.write_bytes(conteudo)
    print(f"Dataset contempArt salvo em {args.output}")


if __name__ == "__main__":
    main()

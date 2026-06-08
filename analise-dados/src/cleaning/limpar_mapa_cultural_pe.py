"""Transforma o JSON do Mapa Cultural de Pernambuco em CSV limpo."""

from __future__ import annotations

import argparse
import json
from pathlib import Path

import pandas as pd


PROJECT_DIR = Path(__file__).resolve().parents[2]
DEFAULT_INPUT = PROJECT_DIR / "data" / "raw" / "mapa_cultural_pe_agentes.json"
DEFAULT_OUTPUT = PROJECT_DIR / "data" / "processed" / "mapa_cultural_pe_agentes.csv"


def texto(valor: object) -> str | None:
    if valor is None:
        return None
    resultado = str(valor).strip()
    return resultado or None


def lista_para_texto(valor: object) -> str | None:
    if not isinstance(valor, list):
        return texto(valor)
    itens = [str(item).strip() for item in valor if str(item).strip()]
    return " | ".join(sorted(set(itens))) or None


def data_do_objeto(valor: object) -> str | None:
    if isinstance(valor, dict):
        return texto(valor.get("date"))
    return texto(valor)


def achatar_agente(agente: dict) -> dict:
    tipo = agente.get("type") if isinstance(agente.get("type"), dict) else {}
    termos = agente.get("terms") if isinstance(agente.get("terms"), dict) else {}

    return {
        "id": agente.get("id"),
        "nome": texto(agente.get("name")),
        "descricao_curta": texto(agente.get("shortDescription")),
        "tipo_id": tipo.get("id"),
        "tipo_nome": texto(tipo.get("name")),
        "termos_tags": lista_para_texto(termos.get("tag")),
        "termos_areas": lista_para_texto(termos.get("area")),
        "termos_funcoes": lista_para_texto(termos.get("funcao")),
        "termos_etnias": lista_para_texto(termos.get("etnia")),
        "termos_subareas": lista_para_texto(termos.get("subarea")),
        "data_criacao": data_do_objeto(agente.get("createTimestamp")),
        "data_atualizacao": data_do_objeto(agente.get("updateTimestamp")),
    }


def main() -> None:
    parser = argparse.ArgumentParser()
    parser.add_argument("--input", type=Path, default=DEFAULT_INPUT)
    parser.add_argument("--output", type=Path, default=DEFAULT_OUTPUT)
    args = parser.parse_args()

    documento = json.loads(args.input.read_text(encoding="utf-8"))
    dados = documento.get("dados", documento)
    if not isinstance(dados, list):
        raise ValueError("O JSON deve conter uma lista em 'dados'.")

    dataframe = pd.DataFrame(achatar_agente(agente) for agente in dados)
    dataframe = dataframe.drop_duplicates(subset=["id"], keep="last")
    dataframe = dataframe.sort_values("id", kind="stable").reset_index(drop=True)

    args.output.parent.mkdir(parents=True, exist_ok=True)
    dataframe.to_csv(args.output, index=False, encoding="utf-8")
    print(f"{len(dataframe)} registros limpos salvos em {args.output}")


if __name__ == "__main__":
    main()

from __future__ import annotations

from itertools import combinations

import numpy as np
import pandas as pd
from sklearn.metrics.pairwise import cosine_similarity
from sklearn.preprocessing import StandardScaler

SIM_NUMERIC_COLS = [
    "percentual_completude",
    "n_obras",
    "n_obras_publicadas",
    "curtidas_recebidas",
    "comentarios_recebidos",
    "seguidores",
    "seguindo",
    "n_tags_expertise",
    "n_tags_necessidade",
    "idade_conta_dias",
]
SIM_LOG_COLS = [
    "n_obras",
    "n_obras_publicadas",
    "curtidas_recebidas",
    "comentarios_recebidos",
    "seguidores",
    "seguindo",
]


def make_profile_label(row: pd.Series) -> str:
    area = str(row.get("area_artistica", "Não informado"))
    cidade = str(row.get("cidade", "Não informado"))
    perfil_id = str(row.get("perfil_id", "sem-id"))[:6]
    return f"{area} · {cidade} · {perfil_id}"


def prepare_similarity_data(df: pd.DataFrame) -> tuple[pd.DataFrame, np.ndarray]:
    work = df.reset_index(drop=True).copy()
    work["perfil_label"] = work.apply(make_profile_label, axis=1)

    X = work[SIM_NUMERIC_COLS].copy()
    for col in SIM_LOG_COLS:
        if col in X.columns:
            X[col] = np.log1p(X[col])

    cat = pd.get_dummies(work[["area_artistica", "cidade"]], dtype=float)
    X = pd.concat([X.reset_index(drop=True), cat.reset_index(drop=True)], axis=1)
    Xs = StandardScaler().fit_transform(X)
    sim = cosine_similarity(Xs)
    return work, sim


def default_reference_index(sim: np.ndarray) -> int:
    return int(np.argmax(sim.mean(axis=1)))


def nearest_neighbors(sim: np.ndarray, ref_idx: int, top_k: int = 8) -> np.ndarray:
    scores = sim[ref_idx].copy()
    scores[ref_idx] = -1
    return np.argsort(scores)[-top_k:][::-1]


def similarity_subset(
    work: pd.DataFrame,
    sim: np.ndarray,
    ref_idx: int,
    top_k: int = 8,
) -> tuple[pd.DataFrame, np.ndarray, np.ndarray]:
    neigh = nearest_neighbors(sim, ref_idx=ref_idx, top_k=top_k)
    nodes = np.array([ref_idx, *neigh], dtype=int)
    sub = work.iloc[nodes].reset_index(drop=False).rename(columns={"index": "source_index"})
    sim_sub = sim[np.ix_(nodes, nodes)]
    return sub, sim_sub, neigh


def similarity_table(
    work: pd.DataFrame,
    sim: np.ndarray,
    ref_idx: int,
    top_k: int = 8,
) -> pd.DataFrame:
    neigh = nearest_neighbors(sim, ref_idx=ref_idx, top_k=top_k)
    out = work.iloc[neigh][
        [
            "perfil_label",
            "area_artistica",
            "cidade",
            "curtidas_recebidas",
            "seguidores",
            "n_obras_publicadas",
            "percentual_completude",
            "entrou_fila",
        ]
    ].copy()
    out.insert(1, "similaridade", sim[ref_idx, neigh].round(3))
    out["entrou_fila"] = out["entrou_fila"].map({1: "Entrou", 0: "Não entrou"})
    return out.reset_index(drop=True)


def ego_network_layout(
    work: pd.DataFrame,
    sim: np.ndarray,
    ref_idx: int,
    top_k: int = 8,
    peer_threshold: float = 0.82,
) -> tuple[pd.DataFrame, list[tuple[int, int, float, str]]]:
    neigh = nearest_neighbors(sim, ref_idx=ref_idx, top_k=top_k)
    rows = []
    rows.append(
        {
            "node_idx": ref_idx,
            "x": 0.0,
            "y": 0.0,
            "perfil_label": work.iloc[ref_idx]["perfil_label"],
            "tipo": "ego",
            "similaridade_ego": 1.0,
            "curtidas_recebidas": work.iloc[ref_idx]["curtidas_recebidas"],
            "seguidores": work.iloc[ref_idx]["seguidores"],
            "entrou_fila": work.iloc[ref_idx]["entrou_fila"],
        }
    )

    angles = np.linspace(0, 2 * np.pi, len(neigh), endpoint=False)
    order = np.argsort(sim[ref_idx, neigh])[::-1]
    neigh_ord = neigh[order]
    angles_ord = angles
    for angle, idx in zip(angles_ord, neigh_ord):
        radius = 1.9 - 0.55 * sim[ref_idx, idx]
        rows.append(
            {
                "node_idx": int(idx),
                "x": float(radius * np.cos(angle)),
                "y": float(radius * np.sin(angle)),
                "perfil_label": work.iloc[idx]["perfil_label"],
                "tipo": "vizinho",
                "similaridade_ego": float(sim[ref_idx, idx]),
                "curtidas_recebidas": work.iloc[idx]["curtidas_recebidas"],
                "seguidores": work.iloc[idx]["seguidores"],
                "entrou_fila": work.iloc[idx]["entrou_fila"],
            }
        )

    edges: list[tuple[int, int, float, str]] = []
    for idx in neigh:
        edges.append((ref_idx, int(idx), float(sim[ref_idx, idx]), "ego"))
    for i, j in combinations(neigh.tolist(), 2):
        score = float(sim[i, j])
        if score >= peer_threshold:
            edges.append((int(i), int(j), score, "peer"))

    return pd.DataFrame(rows), edges

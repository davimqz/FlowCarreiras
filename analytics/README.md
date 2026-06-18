# Análise e Visualização de Dados - FlowCarreiras

Esta pasta reúne a trilha acadêmica de Análise e Visualização de Dados (AVD) do
FlowCarreiras. A unidade de análise é o **perfil de artista**: uma linha por
usuário, extraída do PostgreSQL do sistema e materializada em um CSV
reprodutível.

## Fonte e escopo

- **Fonte:** banco PostgreSQL do próprio FlowCarreiras.
- **Base atual:** 400 perfis simulados pelo `scripts/seed_simulado.py`.
- **Arquivo analítico preservado:** `data/processed/perfil_features.csv`.
- **Objetivo:** estudar completude, portfólio, engajamento, rede, mentoria,
  oportunidades, segmentos e entrada na fila de descoberta.

Os dados simulados permitem desenvolver e validar a trilha técnica, mas não
representam impacto real em usuários de produção.

## Estrutura

```text
analytics/
├── README.md
├── requirements.txt
├── data/
│   └── processed/
│       └── perfil_features.csv
├── notebooks/
│   ├── analise_exploratoria.ipynb
│   ├── regressao_engajamento.ipynb
│   └── classificacao_fila_descoberta.ipynb
├── docs/
│   ├── plano_analise_perfis_artistas.md
│   ├── preparacao_dicionario_dados.md
│   ├── proposta_integracao_dashboard.md
│   ├── dashboard_consolidado.md
│   ├── validacao_funcional_analytics.md
│   └── documentacao_final_analytics.md
├── reports/
│   ├── resumo_insights.md
│   └── resultados_modelos.md
├── src/
│   └── extract/
│       └── extrair_tabela_analitica.py
└── scripts/
    └── build/
        ├── build_analise_exploratoria.py
        ├── build_regressao_engajamento.py
        └── build_classificacao_fila_descoberta.py
```

O dashboard Streamlit permanece em `../dashboard/` porque é uma aplicação
publicável, com Docker e dependências próprias.

## Entregáveis

| Entregável | Arquivo | Status |
|---|---|---|
| Plano de análise | `docs/plano_analise_perfis_artistas.md` | Concluído |
| Preparação e dicionário dos dados | `docs/preparacao_dicionario_dados.md` | Concluído |
| Análise exploratória | `notebooks/analise_exploratoria.ipynb` | Concluído |
| Regressão de engajamento | `notebooks/regressao_engajamento.ipynb` | Concluído |
| Classificação da fila de descoberta | `notebooks/classificacao_fila_descoberta.ipynb` | Concluído |
| Dashboard interativo inicial | `../dashboard/app.py` | Concluído |
| Proposta de integração e publicação | `docs/proposta_integracao_dashboard.md` | Concluído |
| Dashboard consolidado | `docs/dashboard_consolidado.md` | Concluído |
| Validação funcional | `docs/validacao_funcional_analytics.md` | Documentado |
| Documentação final | `docs/documentacao_final_analytics.md` | Documentado |

## Como executar os notebooks

```bash
pip install -r analytics/requirements.txt
cd analytics/notebooks
jupyter notebook
```

Execute na ordem:

1. `analise_exploratoria.ipynb`
2. `regressao_engajamento.ipynb`
3. `classificacao_fila_descoberta.ipynb`

## Como executar o dashboard

Com a infraestrutura do sistema:

```bash
docker compose up --build
```

O dashboard analítico fica disponível na rota `/dashboard` configurada pelo
projeto. Ele consulta o PostgreSQL, portanto depende do banco e dos perfis
simulados carregados.

## Reproduzir a tabela analítica

O script `src/extract/extrair_tabela_analitica.py` agrega as tabelas
normalizadas em uma linha por perfil. O CSV versionado permite abrir os
notebooks mesmo sem o banco em execução.

## Leitura responsável

- Correlação não prova causalidade.
- Os clusters descrevem semelhança estatística, não qualidade artística.
- O classificador prevê uma regra simulada de entrada na fila; não deve ser
  usado como ranking ou decisão automática sobre artistas reais.
- Métricas altas no cenário simulado precisam ser reavaliadas quando houver
  dados reais de uso.

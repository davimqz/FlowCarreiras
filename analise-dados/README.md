# Análise de Dados - FlowCarreiras

Esta pasta concentra a preparação e a documentação dos dados usados pelo projeto.

## Datasets

As duas fontes são abertas e reais, mas possuem unidades de análise diferentes:

- **Mapa Cultural de Pernambuco:** agentes culturais retornados pela API pública.
- **contempArt:** artistas em início de carreira vinculados a 15 escolas de arte da Alemanha.

Os datasets são complementares para estudar o ecossistema artístico e cultural, mas não são integrados nem unidos por chave.

## Estrutura

```text
data/raw/         Dados originais baixados das fontes
data/processed/   CSVs limpos e prontos para análise
docs/             Plano, fontes, métricas e dicionários
src/extract/      Scripts de extração
src/cleaning/     Scripts de limpeza e transformação
```

## Reprodução

```bash
pip install -r analise-dados/requirements.txt

python analise-dados/src/extract/baixar_mapa_cultural_pe.py
python analise-dados/src/cleaning/limpar_mapa_cultural_pe.py

python analise-dados/src/extract/baixar_contempart.py
python analise-dados/src/cleaning/limpar_contempart.py
```

O download padrão do Mapa Cultural utiliza exatamente a consulta definida para o projeto: 100 registros da página 1.

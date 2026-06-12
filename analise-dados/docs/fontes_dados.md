# Fontes dos Dados

## Fontes obrigatórias

A entrega principal utiliza duas fontes abertas e reais. Elas possuem contextos e unidades de análise diferentes e não serão unidas linha a linha.

### Mapa Cultural de Pernambuco

- Fonte: API pública do Mapa Cultural de Pernambuco.
- Endpoint: `https://www.mapacultural.pe.gov.br/api/agent/find`.
- Recorte: 1.000 agentes individuais com área artística ou criativa declarada.
- Arquivos: `data/raw/mapa_cultural_pe_agentes.json`, `data/processed/mapa_cultural_pe_agentes.csv` e versão enriquecida.
- Papel: analisar estrutura de perfis, áreas, tags, funções e atualização.

A API não confirma diretamente que cada agente seja artista. O critério adotado aproxima o recorte do público do FlowCarreiras e permanece documentado e reproduzível.
O recorte extraído atualmente não possui município, coordenadas ou localização válida. Portanto, ele não será utilizado para análises territoriais ou mapas geográficos.

### contempArt

- Fonte: repositório público `georgeblck/contempart`.
- Repositório: `https://github.com/georgeblck/contempart`.
- Recorte: 441 artistas em início de carreira ligados a 15 escolas de arte alemãs.
- Arquivos: `data/raw/contempart_artists.csv`, `data/processed/contempart_artists.csv` e versão enriquecida.
- Papel: analisar presença digital, volume registrado e desigualdade de visibilidade.

As imagens e métricas foram coletadas entre 2018 e 2020. `img_count` representa imagens presentes na base, não toda a produção artística.

## Complementaridade das bases reais

| Fonte | Responde melhor sobre | Não comprova |
| --- | --- | --- |
| Mapa Cultural PE | Estrutura, diversidade e taxonomia de perfis culturais em Pernambuco | Dores ou comportamento dentro do FlowCarreiras |
| contempArt | Presença digital e desigualdade de visibilidade | Realidade brasileira ou qualidade artística |

## Regra de uso

- Identificar a fonte de cada resultado.
- Não somar as bases como uma única população.
- Não comparar Pernambuco e Alemanha como universos equivalentes.
- Usar os resultados para orientar hipóteses de produto, não para afirmar impacto real do aplicativo.

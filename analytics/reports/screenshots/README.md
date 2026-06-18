# Evidências Visuais do Dashboard

Capturas produzidas sobre a stack Docker principal do FlowCarreiras em
`2026-06-18`, com a base analítica simulada carregada automaticamente.

## Rotas validadas

- Dashboard analítico: [http://localhost/dashboard](http://localhost/dashboard)
- Visão individual equivalente à aba `/metricas`:
  [http://localhost/dashboard/?email=marina@test.com](http://localhost/dashboard/?email=marina@test.com)

## Arquivos

- [dashboard_01_visao_geral.png](./dashboard_01_visao_geral.png)
  Visão geral do dashboard agregado, com filtros, KPIs e composição inicial.
- [dashboard_02_correlacoes.png](./dashboard_02_correlacoes.png)
  Heatmap de correlações e dispersão entre obras publicadas e curtidas.
- [dashboard_03_segmentos.png](./dashboard_03_segmentos.png)
  Segmentação por clustering com projeção PCA e resumo por segmento.
- [dashboard_04_modelo_descoberta.png](./dashboard_04_modelo_descoberta.png)
  Modelo de descoberta com matriz de confusão, curva ROC, curva precision-recall
  e importância das variáveis.
- [dashboard_05_modo_individual.png](./dashboard_05_modo_individual.png)
  Visão individual do perfil, equivalente ao conteúdo embutido em `/metricas`.

## Observação

A evidência individual foi capturada pela rota direta do Streamlit com
`?email=marina@test.com`, porque a página protegida `/metricas` embute esse mesmo
dashboard em iframe com autenticação. A implementação dessa integração está em
`flowcarreiras-web/src/pages/MetricasPerfil.jsx`.

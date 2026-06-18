# Documentação Final da Trilha de AVD

## 1. Síntese do projeto

O FlowCarreiras foi analisado a partir de dados do próprio sistema com o
objetivo de entender como **perfil, portfólio, atividade, rede e engajamento**
se relacionam com a trajetória dos artistas na plataforma.

Para viabilizar a trilha, foi construída uma base **simulada e reproduzível**,
mantendo o schema real da aplicação e consolidando os registros em uma tabela
analítica por perfil.

### Base consolidada

| Indicador | Valor |
|---|---:|
| Perfis simulados | 400 |
| Variáveis analíticas | 33 |
| Unidade de análise | 1 linha por perfil |
| Fonte | PostgreSQL do sistema |
| Persistência analítica | `perfil_features.csv` |

## 2. O que foi entregue

A trilha final reúne:

- plano de análise;
- preparação e dicionário dos dados;
- extração reproduzível da base relacional;
- análise exploratória com distribuição, correlação e segmentos;
- regressão simples e múltipla;
- classificação e comparação de modelos;
- dashboard Streamlit integrado ao projeto;
- proposta de integração visual;
- validação funcional;
- relatórios de insights e resultados dos modelos.

## 3. Metodologia resumida

### 3.1 Preparação dos dados

Os dados foram extraídos do PostgreSQL, agregados em uma tabela analítica e
tratados com:

- indicadores para ausências significativas;
- regras explícitas para divisões por zero;
- `log1p` em contagens assimétricas;
- padronização para clustering;
- codificação adequada para variáveis categóricas, quando necessário.

### 3.2 Exploração

A exploração buscou responder:

- como o engajamento se distribui;
- quais variáveis se associam mais com curtidas;
- se existem grupos naturais de artistas;
- se a fila de descoberta é previsível no ambiente simulado.

### 3.3 Visualização

O dashboard foi construído para preservar:

- hierarquia de leitura;
- coerência de cores e formas;
- ligação explícita entre gráfico e interpretação;
- integração com o próprio sistema.

## 4. Principais insights

### 4.1 O engajamento é desigual

As curtidas recebidas apresentam padrão de cauda longa:

- média em torno de **17**;
- mediana em torno de **6**;
- máximo de **110**.

Isso mostra que poucos perfis concentram grande parte da atenção.

### 4.2 Produção e rede explicam melhor o engajamento

No cenário simulado:

- obras publicadas têm correlação aproximada de **0,87** com curtidas;
- seguidores têm correlação aproximada de **0,69**;
- completude isolada fica em torno de **0,20**.

Conclusão: completar o perfil ajuda, mas **publicar e construir rede** pesa mais.

### 4.3 Há segmentos distintos de artistas

Os clusters organizam os perfis em níveis de atividade/maturidade:

- um grupo menos ativo;
- um grupo mais ativo, com maior produção, rede e presença na fila.

### 4.4 A fila simulada é previsível

O classificador supera com folga o baseline, o que indica que o alvo simulado
está bem alinhado com os sinais disponíveis na base.

## 5. Resultados dos modelos

### 5.1 Regressão

| Métrica | Valor |
|---|---:|
| Coeficiente simples | 7,96 curtidas por obra publicada |
| R² simples | 0,752 |
| R² teste (múltipla em log) | 0,848 |
| RMSE teste | 0,619 |
| MAE teste | 0,471 |

Leitura:

- a regressão simples já representa bem a tendência;
- a múltipla melhora o ajuste;
- a interpretação deve permanecer associativa, nunca causal.

### 5.2 Classificação

| Métrica | Regressão logística |
|---|---:|
| Baseline | 0,59 |
| Acurácia | 0,90 |
| Precisão | 0,962 |
| Recall | 0,864 |
| F1 | 0,911 |
| ROC-AUC | 0,945 |

Leitura:

- o modelo aprende bem a regra da simulação;
- o desempenho é forte para fins acadêmicos e demonstrativos;
- isso não garante comportamento equivalente com dados reais.

## 6. Implicações para o FlowCarreiras

Os resultados sugerem alguns caminhos:

1. priorizar acompanhamento por **mediana e distribuição**, não só média;
2. incentivar publicação recorrente de portfólio;
3. fortalecer ações ligadas à rede e descoberta;
4. usar segmentação para personalizar jornadas;
5. tratar modelos como apoio à curadoria, nunca como decisão final.

## 7. Limitações

Esta trilha foi construída sobre uma base **simulada**. Portanto:

- não representa comportamento real de produção;
- herda correlações intencionais do seed;
- torna a fila mais previsível do que ela talvez fosse em ambiente real;
- não autoriza automatizar decisões sobre artistas reais;
- não permite inferir causalidade a partir de correlação ou regressão.

Além disso:

- clusters não significam qualidade ou talento;
- diferenças por área e cidade devem ser lidas com cautela;
- o desempenho dos modelos deve ser reavaliado quando houver dados reais.

## 8. Decisões metodológicas importantes

| Decisão | Motivo |
|---|---|
| Uma linha por perfil | facilitar comparação, EDA, modelos e dashboard |
| Preservar ausência como indicador | incompletude é sinal relevante |
| Aplicar `log1p` em contagens | lidar com cauda longa |
| Manter outliers | eles fazem parte do fenômeno observado |
| Usar Streamlit | integrar Python, Postgres e publicação |
| Separar visão individual e agregada | atender produto e análise |
| Documentar viés do alvo | evitar interpretação indevida do modelo |

## 9. Registro final da apresentação

As evidências finais da entrega devem ser registradas diretamente:

- neste documento;
- nos relatórios de insights e modelos;
- e na própria demonstração do dashboard.

### 9.1 Links de publicação validados

- Dashboard analítico publicado em
  [http://localhost/dashboard](http://localhost/dashboard)
- Modo individual validado pela renderização direta em
  [http://localhost/dashboard/?email=marina@test.com](http://localhost/dashboard/?email=marina@test.com)
- Índice das capturas e evidências:
  [`../reports/screenshots/README.md`](../reports/screenshots/README.md)

### 9.2 Capturas registradas

- [`dashboard_01_visao_geral.png`](../reports/screenshots/dashboard_01_visao_geral.png)
  Hero, filtros laterais, KPIs iniciais e composição do dashboard agregado.
- [`dashboard_02_correlacoes.png`](../reports/screenshots/dashboard_02_correlacoes.png)
  Heatmap de correlação e relação entre obras publicadas e curtidas.
- [`dashboard_03_segmentos.png`](../reports/screenshots/dashboard_03_segmentos.png)
  Segmentação por clustering com projeção PCA e resumo por segmento.
- [`dashboard_04_modelo_descoberta.png`](../reports/screenshots/dashboard_04_modelo_descoberta.png)
  Matriz de confusão, curva ROC, precision-recall e importância das variáveis.
- [`dashboard_05_modo_individual.png`](../reports/screenshots/dashboard_05_modo_individual.png)
  Visão individual equivalente ao painel embutido na aba `/metricas`.

Os pontos essenciais a mostrar são:

- visão geral do dashboard;
- distribuições;
- correlações;
- segmentos;
- modelo de descoberta;
- modo individual em `/metricas`.

## 10. Próximos passos recomendados

- coletar e estabilizar dados reais de uso;
- repetir a trilha com usuários reais;
- revisar a definição da fila de descoberta com critérios de negócio;
- comparar modelos com validação cruzada;
- acompanhar equidade por áreas e grupos;
- testar a utilidade do dashboard com equipe e artistas.

## 11. Conclusão final

A trilha de AVD do FlowCarreiras foi concluída com coerência entre:

- base de dados;
- preparação;
- notebooks;
- relatórios;
- dashboard;
- documentação metodológica.

O principal resultado não é apenas um conjunto de gráficos ou métricas, mas uma
estrutura analítica completa, reproduzível e integrada ao projeto, pronta para
ser refinada quando o sistema passar a acumular dados reais.

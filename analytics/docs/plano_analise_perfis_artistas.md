# Plano de Análise

**Projeto:** FlowCarreiras — análise de métricas de perfil de artistas  
**Fonte dos dados:** base do próprio sistema (PostgreSQL), populada com dataset
**simulado** de 400 perfis (ver [Preparação e Dicionário dos Dados](preparacao_dicionario_dados.md))  
**Unidade de análise:** **perfil de artista** (uma linha por usuário)

---

## 1. Contexto

O FlowCarreiras registra sinais relevantes da trajetória de cada artista dentro
da plataforma: preenchimento do perfil, produção de portfólio, respostas de
engajamento, rede social, uso de mentoria e interação com oportunidades. O
desafio desta trilha é transformar esse conjunto disperso de sinais em uma
**visão analítica coerente**, capaz de responder perguntas de produto,
curadoria e acompanhamento.

A escolha metodológica central foi tratar o **perfil** como unidade de análise.
Essa decisão permite:

- preservar a visão individual que o produto já usa em “Minhas Métricas”;
- agregar múltiplas tabelas relacionais em uma leitura comparável;
- construir EDA, clusters e modelos sobre a mesma base.

## 2. Objetivo geral

Identificar **quais características se associam ao engajamento e à entrada na
fila de descoberta**, e comunicar esses achados de forma visualmente clara,
honesta e útil para a plataforma.

## 3. Objetivos específicos

1. Descrever a distribuição das principais métricas por perfil.
2. Verificar quais sinais se relacionam mais com curtidas e seguidores.
3. Detectar segmentos naturais de perfis.
4. Avaliar se a entrada na fila de descoberta é previsível na simulação.
5. Traduzir os resultados em visualizações e implicações práticas.

## 4. Dados considerados

Origem relacional do sistema:

- `usuarios`
- `perfis_artistas`
- `obras`
- `curtidas`
- `comentarios`
- `seguidores`
- `tags` e tabelas de junção
- `mentorias`
- `mensagens_mentoria`
- `oportunidades`
- `notificacoes_oportunidades`

Essas tabelas são agregadas em uma **tabela analítica por perfil**. O volume
atual é de **400 perfis simulados**, suficiente para demonstrar:

- distribuições com cauda longa;
- diferenças entre grupos;
- correlações não triviais;
- modelos simples com leitura interpretável.

## 5. Hipóteses de trabalho

Antes da exploração, assumimos como hipóteses plausíveis:

1. Perfis com maior produção de portfólio tendem a receber mais engajamento.
2. A rede de seguidores acompanha o crescimento de curtidas.
3. A completude do perfil ajuda, mas não é suficiente sozinha.
4. Existem grupos de artistas com padrões distintos de atividade.
5. A entrada na fila de descoberta deve ser explicável por sinais de produção,
   rede e engajamento no cenário simulado.

Essas hipóteses orientam a análise, mas não devem ser confundidas com prova.

## 6. Perguntas de análise

1. **Quais atributos do perfil mais se associam ao engajamento recebido?**  
   Ex.: completude, número de obras, tags, seguidores e comentários.

2. **Perfis mais completos produzem e engajam mais?**  
   A ideia é entender se a completude funciona como motor, condição mínima ou
   apenas indicador fraco.

3. **Como o engajamento se distribui entre os artistas?**  
   A análise procura concentração, cauda longa e distância entre média e
   mediana.

4. **Existem segmentos naturais de artistas?**  
   Ex.: iniciantes, ativos em ascensão, consolidados.

5. **Quais variáveis melhor predizem a entrada na fila de descoberta?**

6. **Há diferenças por área artística e por cidade?**

7. **Como cadastros, curtidas e seguidores evoluem ao longo do tempo?**

## 7. Métricas observadas

| Dimensão | Métricas extraídas |
|---|---|
| **Perfil / completude** | `percentual_completude`, `onboarding_concluido`, `tem_bio`, `tem_foto`, `tem_cidade`, `tem_area`, `n_links`, `n_tags_necessidade`, `n_tags_expertise`, `area_artistica`, `cidade`, `idade_conta_dias` |
| **Portfólio** | `n_obras`, `n_obras_publicadas`, `n_rascunhos`, `diversidade_midia` |
| **Engajamento recebido** | `curtidas_recebidas`, `comentarios_recebidos`, `media_curtidas_por_obra` |
| **Rede** | `seguidores`, `seguindo`, `razao_seg` |
| **Atividade própria** | `curtidas_dadas`, `comentarios_feitos` |
| **Mentoria** | `disponivel_para_mentorar`, `perfil_mentor_configurado`, `n_mentorias_mentor`, `n_mentorias_artista` |
| **Oportunidades** | `recebe_notificacoes`, `n_notificacoes` |
| **Alvos de modelagem** | `entrou_fila`, `curtidas_recebidas`, `seguidores` |

## 8. Recorte inicial da base

Estatísticas já observadas no conjunto atual:

- completude média **80,2** com desvio-padrão **15,8**;
- número médio de obras **3,4**;
- curtidas recebidas com média **17,1**, desvio **23,0** e máximo **110**;
- fila de descoberta com **59%** de positivos;
- correlação de curtidas com obras publicadas em torno de **0,87**;
- correlação de curtidas com seguidores em torno de **0,69**;
- correlação de curtidas com completude em torno de **0,20**.

Esses números já sugerem:

- assimetria forte em variáveis de contagem;
- maior capacidade explicativa de produção e rede;
- potencial de leitura visual e modelagem simples.

## 9. Estratégia analítica

O trabalho foi organizado em quatro blocos:

1. **Descrição da base**
   Distribuições, medianas, dispersão e outliers.

2. **Relações entre variáveis**
   Correlações, dispersões e diferenças por categoria.

3. **Segmentação**
   Agrupamento de perfis com base em sinais de atividade e engajamento.

4. **Modelagem**
   Regressão para tendências e classificação para fila de descoberta.

## 10. Visualizações planejadas

| Pergunta | Métricas | Gráfico planejado |
|---|---|---|
| Distribuição do engajamento | `curtidas_recebidas`, `seguidores` | **Histograma** + **boxplot** com opção log |
| Relações com engajamento | features × `curtidas_recebidas` | **Dispersão + linha de tendência** |
| Padrão geral de associação | bloco de variáveis numéricas | **Heatmap de correlação** |
| Segmentos | features padronizadas | **Dispersão PCA colorida por cluster** |
| Desempenho por categoria | `area_artistica`, `cidade` | **Barras ordenadas** e **boxplots** |
| Evolução temporal | cadastros, curtidas, seguidores | **Linhas** e **áreas acumuladas** |
| Predição da fila | `entrou_fila` | **Matriz de confusão**, **ROC** e **Precision-Recall** |

## 11. Objetivos de comunicação visual

As visualizações precisam:

- destacar rapidamente o que é central;
- reduzir ruído decorativo;
- comunicar incerteza e limitação quando necessário;
- permitir leitura executiva e leitura detalhada no mesmo dashboard.

Em termos práticos, o dashboard deve responder:

- “o que está acontecendo?”;
- “o que parece explicar isso?”;
- “o que muda quando eu filtro?”;
- “o que isso sugere para produto e curadoria?”.

## 12. Justificativa dos tipos de gráfico

As escolhas seguem a hierarquia perceptual de **Cleveland & McGill**, princípios
de **pré-atenção** e a lógica de redução de ruído de **Tufte**.

- **Histograma e boxplot**: adequados para contagens assimétricas e outliers.
- **Dispersão com tendência**: melhor forma de ler associação entre duas
  quantitativas.
- **Heatmap**: bom para padrão geral de correlação, sem exigir foco em valor
  exato de cada célula.
- **Barras ordenadas**: superiores para comparação entre categorias.
- **Linhas e áreas**: naturais para processos no tempo.
- **Matriz de confusão e curvas ROC/PR**: padrões consolidados para leitura de
  classificação.

## 13. Critérios de leitura responsável

Este plano assume desde o início alguns cuidados:

- correlação não prova causalidade;
- clusters não significam qualidade artística;
- desempenho de modelo em base simulada não equivale a desempenho real;
- diferenças por área ou cidade não devem ser tratadas como ranking de valor.

## 14. Resultado esperado

Ao final da trilha, espera-se ter:

- uma base analítica reproduzível;
- notebooks coerentes com o dashboard;
- documentação suficiente para explicar método e limites;
- uma narrativa visual capaz de sustentar apresentação acadêmica e leitura
  gerencial sem mudar a base original.

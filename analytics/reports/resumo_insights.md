# Resumo de Insights

## Visão executiva

Esta análise usa uma base **simulada e reprodutível** de **400 perfis de
artistas**, agregada em uma tabela analítica com **33 variáveis**. O objetivo
não é medir desempenho real de artistas, mas **testar a trilha analítica** do
FlowCarreiras, verificar se os sinais do sistema geram padrões legíveis e
traduzir esses padrões em recomendações de produto, curadoria e acompanhamento.

Em síntese, a base mostra um cenário em que:

- o **engajamento é concentrado** em poucos perfis;
- a **produção publicada** e a **rede** explicam melhor o engajamento que a
  completude isolada;
- existem **segmentos naturais** de artistas, organizados por nível de
  atividade e maturidade;
- a **entrada na fila de descoberta** é previsível no ambiente simulado porque o
  alvo foi gerado com sinais próximos às próprias features disponíveis.

## Retrato da base

| Indicador | Valor |
|---|---:|
| Perfis analisados | 400 |
| Variáveis analíticas | 33 |
| Completude média | 80,2% |
| Mediana de obras | 3 |
| Mediana de obras publicadas | 2 |
| Proporção na fila de descoberta | 59% |

Leitura rápida:

- a completude já nasce relativamente alta na simulação, o que reduz seu poder
  de discriminar perfis sozinha;
- a atividade de portfólio e rede é mais espalhada, o que tende a gerar maior
  capacidade explicativa para essas dimensões;
- a base foi construída para ter **variabilidade suficiente** para gráficos,
  correlações, agrupamentos e modelos.

## Principais achados

### 1. O engajamento tem cauda longa

As curtidas recebidas apresentam um padrão típico de plataformas com efeito de
concentração:

- média de curtidas em torno de **17**;
- mediana de curtidas em torno de **6**;
- máximo observado de **110**.

Isso significa que a média sozinha descreve mal o perfil “típico”. Em termos de
comunicação analítica, a leitura correta exige:

- **mediana** para descrever o centro;
- **boxplot** e **histograma** para mostrar dispersão;
- **escala logarítmica** quando a intenção for revelar melhor a massa de valores
  baixos sem esconder a cauda.

### 2. Produção publicada é o principal motor do engajamento

Os sinais mais associados às curtidas recebidas são:

- `n_obras_publicadas` com correlação aproximada de **0,87**;
- `seguidores` com correlação aproximada de **0,69**;
- `percentual_completude` com correlação aproximada de **0,20**.

Interpretação:

- **completar o perfil ajuda**, mas seu efeito é relativamente fraco quando
  comparado a publicar obras e construir rede;
- no cenário simulado, o artista mais visível é sobretudo aquele que
  **produz**, **publica** e **acumula conexões**;
- isso sugere que, no produto, nudges e recomendações de uso não devem se
  limitar a onboarding, mas também incentivar **movimento contínuo**.

### 3. Há segmentos naturais de artistas

O agrupamento mostrou melhor separação com **2 clusters**
(`silhouette = 0,367`), o que sugere um eixo principal de diferenciação:

- um grupo menos ativo, com menos obras, menos curtidas e menor rede;
- um grupo mais ativo, com mais obras, mais engajamento e maior presença na
  fila de descoberta.

Médias observadas no cenário simulado:

| Segmento | Obras médias | Curtidas médias | Seguidores médios |
|---|---:|---:|---:|
| Menos ativo | 1,0 | 1,0 | 2,1 |
| Mais ativo | 5,7 | 32,6 | 7,5 |

Esses segmentos são úteis porque permitem transformar a análise em ação:

- perfis menos ativos podem receber trilhas de incentivo a publicação e rede;
- perfis mais ativos podem receber destaque, convites, oportunidades ou
  jornadas de mentoria;
- a coordenação passa a acompanhar **grupos com comportamento semelhante**, e
  não só médias globais.

### 4. Diferenças por categoria exigem cautela

No recorte por área artística, **Fotografia** apareceu com maior média de
curtidas no cenário simulado. Ainda assim, essa leitura deve ser tratada com
cuidado:

- a base não foi construída para afirmar superioridade entre áreas;
- parte da diferença pode vir de composição amostral e ruído do seed;
- a variação **dentro** das áreas permanece alta.

Uso recomendado:

- monitorar possíveis assimetrias;
- não transformar essas diferenças em ranking de valor artístico;
- reavaliar com dados reais antes de qualquer conclusão operacional.

## Implicações para produto e curadoria

Os achados apontam para algumas direções práticas:

1. **Acompanhar distribuição, não só média.**
   O dashboard deve privilegiar mediana, percentis e dispersão para evitar
   leituras distorcidas por poucos perfis muito fortes.

2. **Estimular produção recorrente.**
   Se publicar obras é o principal motor do engajamento, o produto deve reduzir
   fricção de postagem, reforçar rascunhos, lembrar pendências e valorizar a
   continuidade.

3. **Fortalecer mecanismos de rede.**
   Seguidores aparecem como dimensão importante do engajamento; por isso, faz
   sentido pensar em recomendações de perfis, rotas de descoberta e ações de
   interação entre artistas.

4. **Personalizar jornadas por segmento.**
   Nem todo artista precisa da mesma intervenção. Segmentação torna o dashboard
   mais útil como ferramenta de priorização.

5. **Usar o classificador com responsabilidade.**
   O modelo serve como apoio de leitura do ambiente simulado, não como árbitro
   de mérito ou substituto de curadoria humana.

## Leitura responsável

Este resumo precisa ser interpretado dentro do contexto metodológico correto:

- a base é **simulada** e não representa comportamento real de produção;
- as correlações foram favorecidas por um gerador que introduz padrões
  propositais;
- a fila de descoberta foi simulada com sinais próximos aos usados nos modelos;
- nenhum achado aqui deve ser transformado em regra definitiva para artistas
  reais sem nova validação.

## Conclusão

O trabalho mostra que a trilha de analytics do FlowCarreiras está coerente: a
base, os notebooks, os relatórios e o dashboard contam a **mesma história**.
Essa história é simples e útil:

- engajamento é desigual;
- publicar e construir rede pesa mais que completar perfil;
- artistas podem ser organizados em segmentos acionáveis;
- e o sistema já tem sinais suficientes para apoiar análises e visualizações
  robustas, desde que se respeitem os limites do cenário simulado.

# Resultados dos Modelos

## Objetivo desta etapa

Os modelos desta trilha não foram construídos para produção nem para decisão
automática sobre artistas reais. O papel deles aqui é **analítico e didático**:

- verificar se as variáveis extraídas do sistema conseguem representar relações
  relevantes;
- traduzir essas relações em métricas objetivas;
- demonstrar como regressão e classificação podem complementar a leitura
  descritiva do dashboard e da EDA.

## 1. Regressão

### Escopo

Alvo principal: `curtidas_recebidas`.

Foram usados dois recortes:

- **regressão linear simples**, com `n_obras_publicadas` como preditor;
- **regressão linear múltipla**, em escala `log1p`, incorporando produção,
  rede, comentários, tags, completude e idade da conta.

### Resultados resumidos

| Modelo | Resultado |
|---|---:|
| Regressão simples: obras publicadas → curtidas | R² = 0,752 |
| Coeficiente simples | 7,96 curtidas por obra publicada |
| Regressão múltipla em escala log | R² teste = 0,848 |
| RMSE teste | 0,619 |
| MAE teste | 0,471 |

### Interpretação

#### Regressão simples

O modelo simples já explica uma parcela elevada da variação do alvo. Isso
confirma a leitura da EDA: no cenário simulado, o número de obras publicadas
funciona como um forte resumo de atividade e visibilidade.

O coeficiente de **7,96** curtidas por obra publicada deve ser lido como
**tendência média do ambiente simulado**, e não como efeito causal garantido.
Em outras palavras:

- o modelo descreve associação;
- não prova que publicar uma nova obra causará exatamente esse aumento;
- parte da força do ajuste decorre do modo como a base simulada foi construída.

#### Regressão múltipla

Quando o modelo incorpora mais sinais e usa `log1p` para lidar com assimetria e
heterocedasticidade, o ajuste melhora:

- o **R² de teste = 0,848** indica capacidade forte de explicar o alvo no
  cenário simulado;
- **RMSE** e **MAE** em escala log mostram erro relativamente controlado;
- os preditores ligados a **produção** e **rede** continuam dominando a
  explicação do engajamento.

Leitura prática:

- o modelo múltiplo não substitui a EDA, mas a **confirma** com linguagem
  quantitativa;
- ele ajuda a ordenar o peso relativo das variáveis;
- mostra que completude e antiguidade contribuem menos do que produção e
  seguidores.

### Conclusão da regressão

No ambiente simulado, as curtidas recebidas são bem representadas por um bloco
de variáveis de produção e engajamento. A regressão cumpre bem o papel de:

- resumir tendência;
- comparar preditores;
- apoiar a narrativa visual do dashboard.

## 2. Classificação

### Escopo

Alvo: `entrou_fila`.

Modelos comparados:

- **Regressão logística** como baseline interpretável e robusta;
- **Árvore de decisão** rasa como alternativa simples e mais explicável por
  regras.

Baseline da classe majoritária: **0,59**.

### Resultados resumidos

| Métrica | Regressão logística | Árvore de decisão |
|---|---:|---:|
| Acurácia | 0,900 | 0,900 |
| Precisão | 0,962 | 0,930 |
| Recall | 0,864 | 0,898 |
| F1 | 0,911 | 0,914 |
| ROC-AUC | 0,945 | 0,931 |

### Interpretação

#### Desempenho geral

Ambos os modelos superam com folga o baseline de **0,59**, indicando que o alvo
simulado é fortemente aprendível a partir das features disponíveis.

Em especial:

- a **regressão logística** apresenta **ROC-AUC = 0,945**, excelente para
  ranqueamento;
- a **árvore** tem desempenho muito próximo, o que reforça a consistência do
  sinal presente na base;
- a diferença entre **precisão** e **recall** sugere um trade-off natural entre
  rigor e cobertura.

#### Regressão logística

Pontos fortes:

- melhor capacidade de ranqueamento entre positivos e negativos;
- mais estável;
- coeficientes padronizados facilitam interpretação do peso das variáveis.

Uso ideal nesta trilha:

- modelo principal para a aba de classificação no dashboard;
- referência de leitura quantitativa da fila simulada.

#### Árvore de decisão

Pontos fortes:

- regras mais tangíveis;
- leitura intuitiva para explicar decisões.

Limites:

- tende a ser mais sensível a variações amostrais;
- ficou levemente abaixo da logística em ROC-AUC.

### Interpretação operacional do limiar

No dashboard, o limiar de decisão pode ser deslocado para mostrar o impacto da
estratégia escolhida:

- limiar mais baixo favorece **recall** e reduz a chance de deixar bons perfis
  de fora;
- limiar mais alto favorece **precisão** e reduz a chance de destacar perfis
  fracos;
- a escolha depende da política de produto e curadoria, não apenas da métrica
  estatística.

Esse ponto é importante porque transforma o modelo em uma ferramenta de
**simulação de política**, e não apenas de predição.

## 3. O que os modelos confirmam

Tomados em conjunto, regressão e classificação reforçam a mesma leitura:

- **produção publicada** importa;
- **seguidores e sinais de rede** importam;
- **engajamento recebido** anda junto com esses sinais;
- **completude**, embora relevante, aparece mais como fator complementar.

Isso é valioso porque os modelos não estão contradizendo a EDA nem o dashboard:
eles estão **quantificando a mesma narrativa**.

## 4. Limites e leitura responsável

O ponto mais importante deste relatório é o limite metodológico:

- o alvo `entrou_fila` foi gerado por uma regra simulada associada a produção e
  engajamento;
- essas mesmas dimensões entram como features do modelo;
- portanto, o desempenho alto indica principalmente que o modelo aprendeu bem a
  **regra da simulação**.

Consequências:

- o resultado **não comprova** comportamento igual em produção;
- a performance tende a cair quando o sistema passar a lidar com sinais reais,
  ruído real e comportamento real de usuários;
- qualquer uso futuro exige nova coleta, nova validação e revisão dos critérios
  de negócio.

## 5. Conclusão

Os modelos cumprem bem a função acadêmica e analítica desta trilha:

- a regressão mostra que o engajamento é representável por sinais estruturais da
  plataforma;
- a classificação mostra que a fila simulada é previsível;
- ambos fortalecem a coerência entre dados, notebooks, relatórios e dashboard.

O ganho principal não é “acertar artistas”, mas demonstrar que a base e o
pipeline estão maduros o suficiente para sustentar uma análise completa,
interpretável e visualmente consistente.

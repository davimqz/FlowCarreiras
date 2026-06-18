# Preparação e Dicionário dos Dados

**Projeto:** FlowCarreiras — análise de métricas de perfil  
**Complementa:** [Plano de Análise](plano_analise_perfis_artistas.md)

---

## 1. Propósito desta etapa

Esta etapa transforma a base transacional do FlowCarreiras em uma estrutura
analítica estável, reproduzível e adequada para exploração visual, segmentação
e modelagem. O foco aqui não é criar novos dados, mas **organizar e tornar
legível** o que o sistema já registra.

## 2. Fonte dos dados

A fonte é **própria + simulada**:

- banco **PostgreSQL do próprio sistema**;
- populado com **400 perfis simulados** gerados por
  [`scripts/seed_simulado.py`](../../scripts/seed_simulado.py).

### Por que usar dados simulados?

Em produção o sistema ainda tinha volume insuficiente para:

- correlação estável;
- agrupamento;
- comparação por categoria;
- avaliação de modelos.

O conjunto simulado foi adotado para garantir:

- volume mínimo analítico;
- coerência com o schema real;
- possibilidade de reexecução integral da trilha.

## 3. Garantias de qualidade da fonte

O dataset foi planejado para ser útil e auditável:

- **reprodutível**: `random.seed(42)`;
- **rastreável**: registros marcados com domínio `@sim.flowcarreiras.dev`;
- **idempotente**: nova execução recompõe o conjunto simulado;
- **íntegro**: mantém relações e chaves do schema real;
- **não trivial**: introduz padrões e ruído para evitar uma base totalmente
  determinística.

## 4. Formatos ao longo do pipeline

| Etapa | Formato | Finalidade |
|---|---|---|
| Origem | Tabelas relacionais PostgreSQL | Persistência operacional do sistema |
| Extração | SQL + `pandas.DataFrame` | Agregação e transformação |
| Persistência analítica | CSV (`analytics/data/processed/perfil_features.csv`) | Reprodutibilidade dos notebooks |
| Consumo | Notebooks e dashboard Streamlit | Exploração, visualização e modelagem |

## 5. Da forma relacional à tabela analítica

Os dados do sistema são **normalizados**. Já a análise exige **uma linha por
perfil**. A transformação central consiste em:

1. selecionar os perfis simulados;
2. agregar contagens e indicadores por perfil;
3. agregar sinais de rede por usuário;
4. criar variáveis derivadas úteis para leitura e modelo;
5. consolidar tudo em `perfil_features.csv`.

Essa passagem é importante porque:

- reduz a complexidade para exploração;
- evita múltiplos joins repetidos em cada notebook;
- garante que todos os artefatos usem a **mesma base analítica**.

## 6. Tabelas de origem e papel analítico

| Tabela | Papel na análise |
|---|---|
| `usuarios` | identificação, data de criação, recorte do conjunto simulado |
| `perfis_artistas` | completude, área, cidade, fila, mentoria e preferências |
| `obras` | produção, publicações e diversidade de mídia |
| `curtidas` | engajamento recebido e atividade do usuário |
| `comentarios` | engajamento recebido e atividade do usuário |
| `seguidores` | rede social |
| `perfil_links_externos` | presença de links |
| `perfil_tags_expertise` / `perfil_tags_necessidade` | especialização e demanda |
| `mentorias` | atividade como mentor e mentorado |
| `notificacoes_oportunidades` | interação com oportunidades |

## 7. Dicionário da tabela `perfil_features`

| Variável | Tipo | Origem / cálculo | Leitura | Tratamento |
|---|---|---|---|---|
| `percentual_completude` | numérica [0,100] | `perfis_artistas` | nível de preenchimento | mantida; padronização p/ clustering |
| `onboarding_concluido` | binária | `perfis_artistas` | conclusão do onboarding | mantida como 0/1 |
| `tem_bio`, `tem_foto`, `tem_cidade`, `tem_area` | binária | presença de campo | indicador de preenchimento | derivadas 0/1 |
| `n_links` | contagem | `perfil_links_externos` | presença externa | mantida |
| `n_tags_necessidade`, `n_tags_expertise` | contagem | tabelas de junção | densidade de tags | mantidas |
| `area_artistica`, `cidade` | categórica | `perfis_artistas` | contexto do perfil | one-hot opcional |
| `idade_conta_dias` | numérica | `now - data_criacao` | antiguidade | mantida |
| `n_obras` | contagem | `count(obras)` | produção total | `log1p` em modelos |
| `n_obras_publicadas` | contagem | filtro `status='PUBLICADA'` | produção publicada | `log1p` |
| `n_rascunhos` | contagem | `n_obras - n_obras_publicadas` | estoque não publicado | mantida |
| `diversidade_midia` | contagem | `count(distinct tipo_midia)` | variedade de mídia | mantida |
| `curtidas_recebidas` | contagem | `curtidas` sobre `obras` | engajamento recebido | `log1p`; alvo de regressão |
| `comentarios_recebidos` | contagem | `comentarios` sobre `obras` | profundidade de engajamento | `log1p` |
| `media_curtidas_por_obra` | razão | curtidas / obras publicadas | eficiência média de portfólio | 0 se denominador = 0 |
| `seguidores`, `seguindo` | contagem | `seguidores` | tamanho e direção da rede | `log1p` |
| `razao_seg` | razão | seguidores / seguindo | balanço da rede | 0 se denominador = 0 |
| `curtidas_dadas`, `comentarios_feitos` | contagem | atividade do usuário | participação ativa | `log1p` |
| `disponivel_para_mentorar`, `perfil_mentor_configurado` | binária | `perfis_artistas` | maturidade/serviço | 0/1 |
| `n_mentorias_mentor`, `n_mentorias_artista` | contagem | `mentorias` | atividade de mentoria | mantidas |
| `recebe_notificacoes`, `n_notificacoes` | binária / contagem | oportunidades | sensibilidade a oportunidades | mantidas |
| `entrou_fila` | binária | `data_entrada_fila IS NOT NULL` | alvo de classificação | mantida |

## 8. Estratégias de limpeza

### 8.1 Valores ausentes

Campos como `bio`, `foto_perfil`, `cidade` e `area_artistica` podem estar
vazios porque o artista ainda não preencheu o perfil. Nesse caso, a ausência
não é ruído aleatório: é **sinal de incompletude**.

Decisão:

- não imputar texto ou categoria artificialmente;
- converter ausência em **indicadores booleanos** (`tem_bio`, `tem_foto`, etc.);
- preencher contagens ausentes com **0**.

### 8.2 Razões indefinidas

As razões:

- `media_curtidas_por_obra`
- `razao_seg`

podem envolver divisão por zero. Para garantir robustez:

- denominador zero gera valor **0**;
- evita `NaN` ou `inf` em gráficos e modelos.

### 8.3 Outliers

Valores extremos de curtidas e seguidores **não são removidos** porque fazem
parte do fenômeno de interesse. A cauda longa é um achado, não um defeito.

Resposta metodológica:

- preservar os outliers;
- usar estatísticas robustas;
- recorrer a `log1p` quando necessário.

### 8.4 Duplicatas e consistência

Embora a base seja construída de modo controlado, foram previstos checks para:

- duplicidade por `usuario_id` e `perfil_id`;
- domínio de `percentual_completude` entre 0 e 100;
- domínio binário de `entrou_fila`;
- coerência entre fila e data de entrada.

## 9. Transformações analíticas

| Técnica | Aplicação | Motivo |
|---|---|---|
| `log1p` | contagens | comprimir cauda longa e melhorar legibilidade/modelagem |
| z-score | clustering | evitar domínio de variáveis em escala maior |
| one-hot | área e cidade | preservar natureza nominal |
| engenharia de atributos | razões e indicadores | explicitar sinais latentes do schema |

## 10. Justificativa por tipo de variável

- **Contagens assimétricas**: exigem `log1p` e leitura por mediana.
- **Booleanas**: já estão no formato ideal para modelagem interpretável.
- **Categóricas nominais**: não devem ser codificadas como inteiros ordenados.
- **Indicadores de ausência**: preservam informação de incompletude.
- **Razões**: ajudam a comparar perfis de tamanhos diferentes, desde que a regra
  para zero seja explícita.

## 11. Pipeline resumido

```text
seed_simulado.py
    ↓
PostgreSQL (tabelas normalizadas)
    ↓
extração SQL com JOIN + GROUP BY
    ↓
tabela analítica por perfil
    ↓
limpeza + derivadas + padronização conceitual
    ↓
perfil_features.csv
    ↓
notebooks + dashboard
```

## 12. Reprodutibilidade

O pipeline é reproduzível porque:

- o seed é fixo;
- a marcação de e-mail separa o conjunto simulado;
- o script de extração é versionado;
- o CSV final fica salvo em `analytics/data/processed/`.

Isso permite repetir a trilha completa sem alterar a base conceitual do
trabalho.

## 13. Limites desta preparação

Mesmo organizada, a base mantém as limitações do cenário simulado:

- não representa uso real em produção;
- herda correlações intencionais do seed;
- pode superestimar clareza e previsibilidade dos padrões.

Ainda assim, ela é adequada para demonstrar a arquitetura analítica, os
tratamentos de dados e a consistência entre documentação, notebooks e dashboard.

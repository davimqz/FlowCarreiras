"""Gera o notebook analise_exploratoria.ipynb com nbformat."""
from pathlib import Path

import nbformat as nbf

ANALYTICS_DIR = Path(__file__).resolve().parents[2]
OUTPUT_PATH = ANALYTICS_DIR / "notebooks" / "analise_exploratoria.ipynb"

nb = nbf.v4.new_notebook()
cells = []
def md(s): cells.append(nbf.v4.new_markdown_cell(s.strip("\n")))
def code(s): cells.append(nbf.v4.new_code_cell(s.strip("\n")))

md(r"""
# Análise Exploratória de Dados

**Projeto:** FlowCarreiras — métricas de perfil de artistas
**Base:** `../data/processed/perfil_features.csv` (400 perfis simulados, 1 linha por perfil — ver [preparação e dicionário dos dados](../docs/preparacao_dicionario_dados.md))

Este notebook realiza a análise exploratória prevista no [plano de análise](../docs/plano_analise_perfis_artistas.md):
distribuições, correlações, relações por categoria e **agrupamento (clustering)**,
cada bloco acompanhado de **interpretação textual**.
""")

code(r"""
import pandas as pd
import numpy as np
import matplotlib.pyplot as plt
import seaborn as sns
from itertools import combinations
from sklearn.preprocessing import StandardScaler
from sklearn.cluster import KMeans
from sklearn.decomposition import PCA
from sklearn.metrics import silhouette_score
from sklearn.metrics.pairwise import cosine_similarity

%matplotlib inline
sns.set_theme(style='whitegrid')
plt.rcParams['figure.dpi'] = 90
ROXO = '#7c3aed'
pd.set_option('display.max_columns', 60)

df = pd.read_csv('../data/processed/perfil_features.csv')
df['perfil_curto'] = (
    df['area_artistica'].fillna('Não informado').astype(str) + ' · ' +
    df['cidade'].fillna('Não informado').astype(str) + ' · ' +
    df['perfil_id'].astype(str).str[:6]
)
print('Dimensões:', df.shape)
df.head()
""")

md(r"""
## 1. Estatística descritiva

Primeiro olhar sobre a tendência central e a dispersão das variáveis numéricas.
""")

code(r"""
df.describe().T[['mean', 'std', 'min', '25%', '50%', '75%', 'max']].round(2)
""")

md(r"""
**Leitura inicial.** Observa-se de imediato a marca registrada de dados de engajamento:
a **média é muito maior que a mediana** em `curtidas_recebidas`, `seguidores` e `n_obras`,
e o **desvio-padrão é da ordem da própria média** — sinais clássicos de distribuições
**assimétricas à direita (cauda longa)**. Já `percentual_completude` se concentra em
valores altos (mediana ~85), distribuição assimétrica à esquerda. Isso justifica, mais
adiante, o uso de **escala logarítmica** e de **mediana/IQR** em vez de média/desvio.
""")

md(r"""
## 2. Distribuições

Histograma (forma) + boxplot (mediana, IQR, outliers) para as variáveis-chave.
""")

code(r"""
vars_dist = ['curtidas_recebidas', 'n_obras', 'seguidores', 'percentual_completude']
fig, axes = plt.subplots(2, 4, figsize=(18, 7))
for i, v in enumerate(vars_dist):
    sns.histplot(df[v], kde=True, ax=axes[0, i], color=ROXO)
    axes[0, i].set_title(f'Histograma — {v}')
    sns.boxplot(x=df[v], ax=axes[1, i], color='#a78bfa')
    axes[1, i].set_title(f'Boxplot — {v}')
plt.tight_layout()
plt.show()
""")

code(r"""
# Efeito da transformação log1p nas contagens assimétricas
fig, axes = plt.subplots(1, 3, figsize=(16, 4))
for i, v in enumerate(['curtidas_recebidas', 'n_obras', 'seguidores']):
    sns.histplot(np.log1p(df[v]), kde=True, ax=axes[i], color=ROXO)
    axes[i].set_title(f'log1p({v})')
plt.tight_layout()
plt.show()
""")

md(r"""
**Interpretação.** As contagens de engajamento têm **forte concentração em valores baixos**
e uma cauda de poucos perfis muito populares (boxplots com vários outliers superiores) —
o engajamento é **desigual**: a maioria recebe pouco e um grupo pequeno concentra muito.
A transformação **`log1p`** aproxima as contagens de uma forma mais simétrica, o que é
desejável para os gráficos de dispersão da etapa de regressão e para algoritmos sensíveis à escala (clustering).
A `percentual_completude`, ao contrário, está deslocada para a direita: a maioria dos perfis
já é bastante completa.
""")

md(r"""
## 3. Correlações

Matriz de Pearson entre os principais atributos e os alvos de modelagem.
""")

code(r"""
feat_corr = ['percentual_completude', 'n_obras', 'n_obras_publicadas', 'n_tags_expertise',
             'n_tags_necessidade', 'seguidores', 'seguindo', 'curtidas_recebidas',
             'comentarios_recebidos', 'idade_conta_dias', 'entrou_fila']
corr = df[feat_corr].corr()
plt.figure(figsize=(10, 8))
sns.heatmap(corr, annot=True, fmt='.2f', cmap='RdBu_r', center=0, square=True,
            cbar_kws={'shrink': 0.8})
plt.title('Matriz de correlação (Pearson)')
plt.tight_layout()
plt.show()
""")

md(r"""
**Interpretação.** O engajamento recebido (`curtidas_recebidas`) é fortemente explicado pelo
**volume de produção**: correlação alta com `n_obras_publicadas` e `n_obras` (~0,9) e
moderada-alta com `seguidores` (~0,7). A `percentual_completude` tem correlação **fraca-positiva**
(~0,2) — completar o perfil ajuda, mas é o **produzir e acumular rede** que move o engajamento.
`idade_conta_dias` quase não correlaciona, indicando que **antiguidade por si só não gera
engajamento**. O alvo `entrou_fila` acompanha as mesmas variáveis de produção/engajamento,
o que antecipa que um classificador simples deve funcionar bem.
""")

md(r"""
## 4. Relações por categoria (análise multivariada)

Engajamento segmentado por **área artística e cidade** — combinando variáveis
categóricas com uma quantitativa.
""")

code(r"""
top_areas = df['area_artistica'].value_counts().index[:8]
sub_areas = df[df['area_artistica'].isin(top_areas)]

top_cidades = (
    df.loc[df['cidade'].ne('Não informado'), 'cidade']
    .value_counts().index[:8]
)
sub_cidades = df[df['cidade'].isin(top_cidades)]

fig, axes = plt.subplots(1, 2, figsize=(18, 6))
sns.boxplot(
    data=sub_areas, x='area_artistica', y='curtidas_recebidas',
    hue='area_artistica', palette='Purples', legend=False, ax=axes[0]
)
axes[0].tick_params(axis='x', rotation=30)
axes[0].set_title('Curtidas recebidas por área artística')

sns.boxplot(
    data=sub_cidades, x='cidade', y='curtidas_recebidas',
    hue='cidade', palette='Blues', legend=False, ax=axes[1]
)
axes[1].tick_params(axis='x', rotation=35)
axes[1].set_title('Curtidas recebidas por cidade')
plt.tight_layout()
plt.show()

resumo_areas = (
    df.groupby('area_artistica')
    [['curtidas_recebidas', 'seguidores', 'percentual_completude']]
    .agg(['count', 'median', 'mean'])
)
resumo_cidades = (
    df[df['cidade'].ne('Não informado')]
    .groupby('cidade')
    [['curtidas_recebidas', 'seguidores', 'percentual_completude']]
    .agg(['count', 'median', 'mean'])
)

display(resumo_areas.round(1))
display(resumo_cidades.round(1))
""")

md(r"""
**Interpretação.** Como os dados foram simulados sem privilegiar área ou cidade, as medianas de
engajamento entre categorias são próximas e existe grande variação dentro de cada grupo.
Diferenças pontuais podem refletir tamanho da amostra e ruído da simulação, não um efeito real
da área artística ou do território. No FlowCarreiras, essas comparações servem para monitorar
distribuição e possíveis desigualdades futuras, não para afirmar que uma categoria produz
artistas melhores ou mais relevantes.
""")

md(r"""
## 5. Similaridade entre perfis

Além de correlação e cluster, vale observar **quem parece com quem** na base.
Isso aproxima a análise de uma lógica de descoberta por afinidade, comum em
produtos sociais. Aqui a similaridade é calculada por **cosseno** sobre um
conjunto de sinais de perfil, produção, rede e contexto.
""")

code(r"""
sim_feats = ['percentual_completude', 'n_obras', 'n_obras_publicadas',
             'curtidas_recebidas', 'comentarios_recebidos', 'seguidores',
             'seguindo', 'n_tags_expertise', 'n_tags_necessidade',
             'idade_conta_dias']
X_sim = df[sim_feats].copy()
for c in ['n_obras', 'n_obras_publicadas', 'curtidas_recebidas',
          'comentarios_recebidos', 'seguidores', 'seguindo']:
    X_sim[c] = np.log1p(X_sim[c])

cats = pd.get_dummies(df[['area_artistica', 'cidade']], dtype=float)
X_sim = pd.concat([X_sim.reset_index(drop=True), cats.reset_index(drop=True)], axis=1)
X_sim = StandardScaler().fit_transform(X_sim)
sim = cosine_similarity(X_sim)
ego_idx = int(np.argmax(sim.mean(axis=1)))  # perfil mais central no espaço de similaridade
scores_ego = sim[ego_idx].copy()
scores_ego[ego_idx] = -1
vizinhos = np.argsort(scores_ego)[-8:][::-1]
grupo = np.array([ego_idx, *vizinhos])
sim_grupo = pd.DataFrame(
    sim[np.ix_(grupo, grupo)],
    index=df.iloc[grupo]['perfil_curto'],
    columns=df.iloc[grupo]['perfil_curto']
).round(2)

plt.figure(figsize=(11, 8))
sns.heatmap(sim_grupo, cmap='mako', vmin=0, vmax=1, square=True,
            linewidths=.5, cbar_kws={'label': 'similaridade'})
plt.title('Mapa de similaridade entre o ego e os perfis mais próximos')
plt.xticks(rotation=55, ha='right')
plt.yticks(rotation=0)
plt.tight_layout()
plt.show()

display(
    pd.DataFrame({
        'perfil': df.iloc[vizinhos]['perfil_curto'].values,
        'similaridade': sim[ego_idx, vizinhos].round(3),
        'curtidas_recebidas': df.iloc[vizinhos]['curtidas_recebidas'].values,
        'seguidores': df.iloc[vizinhos]['seguidores'].values,
        'n_obras_publicadas': df.iloc[vizinhos]['n_obras_publicadas'].values,
    })
)
""")

md(r"""
**Interpretação.** O heatmap deixa claro quais perfis se agrupam por **afinidade
de comportamento**. Em vez de olhar só médias globais, essa leitura permite
enxergar vizinhanças de artistas que combinam nível de produção, rede,
engajamento e contexto. É uma perspectiva útil para recomendações do tipo
"artistas parecidos com este perfil".
""")

md(r"""
## 6. Ego network por similaridade

Agora a mesma lógica é mostrada como **rede egocentrada**: um perfil de
referência no centro e, ao redor, os seus perfis mais parecidos. É uma
representação inspirada em visualizações de rede social, mas baseada em
**similaridade analítica**, não em amizade declarada.
""")

code(r"""
fig, ax = plt.subplots(figsize=(10, 10))
coords = {ego_idx: (0.0, 0.0)}
angles = np.linspace(0, 2*np.pi, len(vizinhos), endpoint=False)
for angle, idx in zip(angles, vizinhos):
    raio = 1.9 - 0.55 * sim[ego_idx, idx]
    coords[idx] = (raio * np.cos(angle), raio * np.sin(angle))

# arestas do ego para os vizinhos
for idx in vizinhos:
    x0, y0 = coords[ego_idx]
    x1, y1 = coords[idx]
    ax.plot([x0, x1], [y0, y1], color='#94a3b8', alpha=0.55, lw=1.2 + 2.4 * sim[ego_idx, idx])

# arestas adicionais entre vizinhos muito parecidos entre si
for i, j in combinations(vizinhos.tolist(), 2):
    if sim[i, j] >= 0.82:
        x0, y0 = coords[i]
        x1, y1 = coords[j]
        ax.plot([x0, x1], [y0, y1], color='#cbd5e1', alpha=0.22, lw=1.0 + 1.4 * sim[i, j], ls='--')

cores = []
tamanhos = []
for idx in [ego_idx, *vizinhos]:
    if idx == ego_idx:
        cores.append(ROXO)
    else:
        cores.append('#10b981' if df.loc[idx, 'entrou_fila'] == 1 else '#94a3b8')
    tamanhos.append(500 + 140 * np.log1p(df.loc[idx, 'curtidas_recebidas']))

for cor, tam, idx in zip(cores, tamanhos, [ego_idx, *vizinhos]):
    x, y = coords[idx]
    ax.scatter(x, y, s=tam, color=cor, edgecolor='white', linewidth=1.3, zorder=3)
    ax.text(x, y + 0.12, df.loc[idx, 'perfil_curto'], ha='center', va='bottom', fontsize=8)

ax.set_title('Ego network por similaridade analítica')
ax.set_xticks([]); ax.set_yticks([])
ax.set_xlim(-2.2, 2.2); ax.set_ylim(-2.2, 2.2)
for spine in ax.spines.values():
    spine.set_visible(False)
plt.tight_layout()
plt.show()
""")

md(r"""
**Interpretação.** A ego network traduz a similaridade em uma leitura mais
relacional. O nó central representa um perfil **mais representativo** do espaço
analítico; os nós ao redor são os vizinhos mais próximos. Quanto mais curta e
mais grossa a ligação, maior a similaridade. As conexões tracejadas entre
vizinhos indicam perfis que também se parecem entre si, sugerindo pequenos
subgrupos de afinidade.
""")

md(r"""
## 7. Agrupamento (clustering)

Segmentação não supervisionada dos perfis. Contagens recebem `log1p`, tudo é padronizado
(z-score, pois k-means é sensível à escala), e o número de clusters é escolhido pela
**silhueta**.
""")

code(r"""
feats = ['percentual_completude', 'n_obras', 'n_obras_publicadas', 'curtidas_recebidas',
         'comentarios_recebidos', 'seguidores', 'seguindo', 'n_tags_expertise']
X = df[feats].copy()
for c in ['n_obras', 'n_obras_publicadas', 'curtidas_recebidas',
          'comentarios_recebidos', 'seguidores', 'seguindo']:
    X[c] = np.log1p(X[c])
Xs = StandardScaler().fit_transform(X)

sils = {}
for k in range(2, 7):
    km = KMeans(n_clusters=k, random_state=42, n_init=10).fit(Xs)
    sils[k] = silhouette_score(Xs, km.labels_)
print('Silhueta por k:', {k: round(v, 3) for k, v in sils.items()})
melhor_k = max(sils, key=sils.get)
print('k escolhido:', melhor_k)

km = KMeans(n_clusters=melhor_k, random_state=42, n_init=10).fit(Xs)
df['cluster'] = km.labels_
""")

code(r"""
# Projeção 2D (PCA) para visualizar os agrupamentos
proj = PCA(n_components=2).fit_transform(Xs)
plt.figure(figsize=(8, 6))
sns.scatterplot(x=proj[:, 0], y=proj[:, 1], hue=df['cluster'], palette='Set2', s=40)
plt.title(f'Perfis projetados em 2D (PCA) — {melhor_k} clusters')
plt.xlabel('Componente principal 1')
plt.ylabel('Componente principal 2')
plt.legend(title='cluster')
plt.tight_layout()
plt.show()
""")

code(r"""
# Perfil médio de cada cluster (para nomear os segmentos)
df.groupby('cluster')[feats + ['entrou_fila']].mean().round(1)
""")

md(r"""
**Interpretação.** A silhueta indica a melhor separação no `k` impresso acima. Lendo a tabela
de médias por cluster, os grupos se ordenam claramente ao longo de um eixo de **atividade /
maturidade**: de um lado, perfis com **baixa completude, poucas obras e pouco engajamento**
(artistas iniciantes ou inativos); do outro, perfis **completos, produtivos e com alto
engajamento e rede** (artistas consolidados), tipicamente com maior taxa de `entrou_fila`.
A projeção PCA confirma visualmente essa gradação. Esses segmentos são **acionáveis**: a
plataforma pode, por exemplo, incentivar completude e publicação nos clusters menos ativos.
""")

md(r"""
## 8. Síntese de insights

- **O engajamento é desigual (cauda longa):** poucos artistas concentram a maior parte das
  curtidas e seguidores; a mediana descreve o artista típico melhor que a média.
- **Produção é o principal motor do engajamento:** `n_obras_publicadas` é a variável mais
  associada às curtidas recebidas (~0,9), seguida da rede de seguidores (~0,7).
- **Completar o perfil ajuda, mas pouco isoladamente** (correlação ~0,2): o diferencial está
  em **produzir e acumular rede**, não apenas em preencher campos.
- **Antiguidade não gera engajamento:** `idade_conta_dias` é praticamente não correlacionada.
- **Área artística não determina desempenho:** a variação intra-área supera a variação
  entre áreas.
- **A similaridade entre perfis é legível:** artistas se organizam em vizinhanças de afinidade
  quando combinamos produção, engajamento, rede e contexto.
- **Existem segmentos naturais de artistas** (iniciantes/inativos → consolidados), úteis para
  ações de produto e coerentes com o alvo de descoberta.
- **O alvo `entrou_fila` é previsível** pelas variáveis de produção/engajamento — base sólida
  para o classificador deste projeto.
""")

nb.cells = cells
nb.metadata = {
    'kernelspec': {'name': 'python3', 'display_name': 'Python 3', 'language': 'python'},
    'language_info': {'name': 'python'},
}
nbf.write(nb, OUTPUT_PATH)
print(OUTPUT_PATH, 'gerado com', len(cells), 'células.')


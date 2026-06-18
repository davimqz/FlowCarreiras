# Proposta de Integração Visual da Solução

**Projeto:** FlowCarreiras — análise e visualização de métricas de perfil  
**Ferramenta de publicação:** **Streamlit**  
**Contexto:** web, integrada ao sistema e também acessível como visão analítica autônoma

---

## 1. Objetivo desta proposta

Definir como a solução visual da trilha analítica se encaixa no produto
FlowCarreiras sem criar uma experiência isolada ou desconectada do sistema.
Isso envolve:

- escolha da ferramenta de publicação;
- arquitetura de integração com backend, banco e frontend;
- organização da informação;
- critérios visuais e de usabilidade.

## 2. Ferramenta escolhida

A ferramenta adotada é o **Streamlit**.

### Justificativa

| Critério | Justificativa |
|---|---|
| **Integração com o stack atual** | O projeto já trabalha com Python nos notebooks; o Streamlit reaproveita o mesmo ecossistema. |
| **Leitura direta da base** | Pode consumir o PostgreSQL do sistema na rede interna do `docker-compose`. |
| **Deploy próprio** | Funciona como serviço adicional no mesmo ambiente do sistema, sem dependência de nuvens externas. |
| **Agilidade de iteração** | Permite ajustar visualizações e textos rapidamente, mantendo proximidade com a análise. |
| **Capacidade analítica** | Exibe nativamente gráficos, filtros, tabelas e métricas de modelos. |

### Alternativas descartadas

- **Power BI / Looker Studio / Tableau Public**: bons para BI, mas menos
  aderentes ao requisito de deploy acoplado ao sistema e à leitura direta da
  base interna.
- **Templates de frontend prontos**: podem ser úteis para UI, mas não resolvem
  sozinhos a camada analítica nem a publicação integrada.

## 3. Arquitetura de integração

O dashboard é publicado como um **serviço Streamlit** próprio dentro do projeto.

```text
Usuário / Coordenação
        ↓
Frontend / Nginx
        ├─ / → aplicação principal
        ├─ /metricas → iframe do modo individual
        └─ /dashboard → proxy para Streamlit
                ↓
           dashboard/app.py
                ↓
           PostgreSQL do sistema
```

### Benefícios dessa arquitetura

- evita exportações manuais;
- preserva o dashboard no mesmo domínio do sistema;
- simplifica autenticação no modo individual;
- mantém uma única origem visual e técnica para os dados.

## 4. Dois modos de uso

### 4.1 Modo individual: `/metricas`

Usado pelo artista autenticado, com filtro por usuário.

Finalidade:

- mostrar métricas pessoais;
- oferecer leitura de onboarding, portfólio, rede, mentoria e oportunidades;
- funcionar como extensão do produto.

### 4.2 Modo analítico: `/dashboard`

Usado pela coordenação ou para apresentação da trilha analítica.

Finalidade:

- mostrar o comportamento agregado dos **400 perfis simulados**;
- comunicar os achados da exploração, regressão e classificação;
- servir como entrega visual central da disciplina.

## 5. Organização da informação

### 5.1 Página analítica

A visão agregada foi organizada em quatro áreas:

1. **Início**  
   Contexto, perguntas de análise, instruções de uso e principais achados.

2. **Dados**  
   KPIs básicos, tabela analítica e download do CSV filtrado.

3. **Dashboard**  
   Visualizações e interpretações: composição, rankings, séries temporais,
   distribuições, correlações, segmentos, modelo e importância das variáveis.

4. **Documentação**  
   Metodologia, pipeline e estrutura dos entregáveis.

### 5.2 Filtros

Os filtros ficam na **sidebar** porque:

- segmentam múltiplas visões ao mesmo tempo;
- permanecem visíveis durante a navegação;
- reduzem necessidade de repetir controles em cada seção.

Filtros previstos:

- área artística;
- cidade;
- completude mínima;
- apenas quem entrou na fila.

## 6. Sistema visual proposto

### 6.1 Direção estética

O dashboard adota uma interface de baixo ruído, com:

- tema escuro;
- uma cor de destaque predominante;
- contrastes claros entre áreas de conteúdo;
- gráficos com fundo transparente;
- ênfase em legibilidade antes de ornamentação.

### 6.2 Critérios visuais

| Critério | Aplicação |
|---|---|
| **Hierarquia** | KPIs e síntese primeiro, detalhes depois |
| **Consistência** | mesmas cores e convenções em gráficos parecidos |
| **Agrupamento** | blocos e containers para reunir conteúdo relacionado |
| **Leitura rápida** | títulos objetivos, textos curtos e métricas visíveis |
| **Interpretação junto do gráfico** | cada seção traz comentário analítico |

### 6.3 Tipos de gráfico por finalidade

- **barras** para comparação entre categorias;
- **histograma + boxplot** para distribuição;
- **heatmap** para correlações;
- **scatter / PCA** para segmentos;
- **linhas e áreas** para evolução temporal;
- **matriz de confusão + ROC/PR** para classificação.

## 7. Integração com autenticação e segurança

No modo individual, o dashboard usa o mesmo contexto do sistema:

- recebe `JWT` do frontend;
- decodifica o e-mail do usuário;
- restringe consultas ao perfil correspondente.

Isso evita exposição indevida de dados de outros usuários dentro da visão de
produto.

No modo analítico:

- a leitura é agregada;
- não há foco em dados pessoais sensíveis;
- o uso é voltado a coordenação, avaliação e demonstração.

## 8. Publicação e deploy

### 8.1 Publicação técnica

- serviço Streamlit no `docker-compose`;
- proxy reverso pelo Nginx em `/dashboard`;
- suporte a WebSocket;
- mesma infraestrutura do sistema principal.

### 8.2 Impacto na experiência

Essa escolha evita:

- CORS de múltiplos domínios;
- dashboards externos com identidade quebrada;
- necessidade de manter ambientes duplicados.

## 9. Responsividade e uso

O dashboard precisa funcionar bem em:

- desktop para navegação analítica completa;
- notebook para apresentação;
- tablet em leitura resumida.

Decisões associadas:

- layout `wide`;
- uso de colunas que se reorganizam;
- textos curtos entre blocos;
- gráficos dimensionados para leitura sem sobreposição.

## 10. Riscos e mitigação

| Risco | Mitigação |
|---|---|
| PWA interceptar `/dashboard` | exclusão explícita da rota do fallback do app React |
| Divergência entre dashboard e notebooks | uso da mesma base analítica e das mesmas métricas |
| Leitura excessivamente técnica | textos interpretativos em português em cada seção |
| Superinterpretação do cenário simulado | documentação explícita das limitações |

## 11. Conclusão

O Streamlit é a escolha mais coerente para esta trilha porque une:

- integração com o banco do sistema;
- alinhamento com o stack analítico em Python;
- publicação sob a mesma infraestrutura;
- capacidade de servir tanto ao produto quanto à avaliação analítica.

A proposta final não separa “análise” e “apresentação” em dois mundos distintos:
ela conecta base, modelo, documentação e visualização em uma entrega única,
reproduzível e integrada ao FlowCarreiras.

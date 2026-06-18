# Dashboard Consolidado: decisões, insights e melhorias

**Ferramenta:** Streamlit (`dashboard/app.py`)  
**Acesso analítico:** `http://localhost/dashboard`  
**Dados:** 400 perfis simulados carregados ao vivo do PostgreSQL do sistema

---

## 1. Estado atual da entrega

O dashboard está consolidado como a **camada de apresentação** da trilha
analítica. Ele cumpre três papéis ao mesmo tempo:

1. traduz os notebooks em uma leitura executiva;
2. preserva acesso à base filtrada;
3. documenta a lógica analítica usada na trilha.

Na versão atual:

- os dados carregam diretamente do Postgres;
- as visualizações estão organizadas em narrativa coerente;
- o modo individual e o modo agregado coexistem no mesmo app;
- a documentação da metodologia aparece dentro da própria interface.

## 2. O que foi consolidado

| Componente | Situação atual |
|---|---|
| Leitura dos dados | funcional, com cache e agregação por perfil |
| Filtros globais | ativos por área, cidade, completude e fila |
| Tabela analítica | exibida e exportável |
| Abas analíticas | organizadas por finalidade |
| Classificação | integrada com métricas e ajuste de limiar |
| Documentação | presente dentro do app |

## 3. Estrutura narrativa do dashboard

O dashboard foi desenhado para seguir uma ordem lógica:

### 3.1 Início

Abre com contexto, objetivo, perguntas de análise e instruções de uso. Essa
entrada é importante porque evita que o usuário “caia nos gráficos” sem saber:

- o que está sendo analisado;
- de onde vieram os dados;
- quais perguntas a interface responde.

### 3.2 Dados

Mostra o recorte atual da base:

- quantidade de perfis filtrados;
- quantidade de variáveis;
- proporção na fila;
- completude mediana;
- tabela analítica e download.

Essa aba funciona como ponto de auditoria e transparência.

### 3.3 Dashboard

Organiza a leitura visual propriamente dita:

- composição por área e fila;
- rankings;
- evolução temporal;
- distribuições;
- correlações;
- segmentos;
- classificação;
- importância das variáveis.

### 3.4 Documentação

Conecta a interface à metodologia, reforçando que o dashboard não é apenas
“uma tela bonita”, mas uma camada visual de um pipeline analítico versionado.

## 4. Decisões visuais

| Decisão | Aplicação | Motivo |
|---|---|---|
| **Tema escuro** | fundo e contraste geral | reduzir brilho e valorizar destaques |
| **Uma cor principal** | roxo como destaque | consistência visual e reconhecimento |
| **Containers** | agrupamento de KPIs e blocos | organizar leitura e reduzir ruído |
| **Título + texto interpretativo** | em cada seção | ligar gráfico a insight |
| **Sidebar fixa** | filtros globais | controle persistente e previsível |
| **Escala log opcional** | distribuições | revelar melhor cauda longa |
| **Tabs** | separação de contexto, dados, análise e documentação | diminuir carga cognitiva |

## 5. Leitura analítica por bloco

### 5.1 KPIs

Os KPIs resumem o recorte atual com foco em:

- volume;
- mediana de engajamento;
- mediana de rede;
- presença na fila;
- completude.

Eles abrem a leitura com o essencial antes de aprofundar os gráficos.

### 5.2 Composição

Os gráficos de composição respondem:

- quantos perfis existem por área;
- qual a distribuição entre quem entrou ou não na fila.

Função: contextualizar o “universo” antes das relações.

### 5.3 Rankings

Os rankings dos artistas mais curtidos e com mais seguidores deixam visível a
concentração de engajamento, tornando intuitiva a ideia de cauda longa.

### 5.4 Séries temporais

As séries acumuladas mostram que a base cresce ao longo do período e permitem
relacionar adoção, produção e engajamento com a passagem do tempo.

### 5.5 Distribuições

Histograma e boxplot são o coração da leitura de desigualdade:

- mostram concentração em valores baixos;
- deixam visíveis os extremos;
- justificam o uso de mediana e escala log.

### 5.6 Correlações

O heatmap sintetiza o padrão geral de associação entre produção, rede,
completude, engajamento e fila, funcionando como ponte entre descrição e
modelagem.

### 5.7 Segmentos

O agrupamento adiciona uma leitura de comportamento:

- perfis mais ativos e maduros;
- perfis menos ativos e menos expostos.

Isso é útil porque transforma uma base contínua em grupos mais acionáveis.

### 5.8 Modelo de descoberta

Essa seção mostra que a entrada na fila simulada é previsível no cenário atual.
Além do valor das métricas, o destaque aqui é o **slider de limiar**, que ajuda
o usuário a perceber o custo de privilegiar precisão ou recall.

## 6. Principais insights sustentados pelo dashboard

1. **Engajamento é desigual.**  
   Poucos perfis concentram muito mais curtidas e seguidores que a maioria.

2. **Produção e rede importam mais que completude isolada.**  
   Publicar obras e acumular seguidores está mais ligado ao engajamento do que
   apenas preencher o perfil.

3. **Existem segmentos legíveis de artistas.**  
   A base se organiza em níveis de atividade/maturidade.

4. **A fila simulada é previsível.**  
   O modelo aprende com facilidade a regra subjacente à simulação.

## 7. Melhorias feitas ao longo do processo

| Problema observado | Ajuste realizado |
|---|---|
| Tela vazia no iframe | correção de rota interceptada pelo service worker |
| Modo individual simplificado demais | restauração das seções completas |
| Classificador fraco na primeira versão | revisão da lógica do alvo simulado |
| Narrativa dispersa | inclusão de textos interpretativos por bloco |
| Estrutura pouco clara | reorganização em abas e blocos com hierarquia melhor |

## 8. O que torna a entrega consistente

O dashboard é consistente porque conversa diretamente com:

- a tabela `perfil_features.csv`;
- os notebooks de exploração, regressão e classificação;
- os relatórios de insights e modelos;
- a documentação metodológica.

Isso reduz o risco de uma apresentação “bonita, mas solta”.

## 9. Limites do dashboard

Mesmo consolidado, ele continua sujeito aos limites da base:

- os dados são simulados;
- os padrões favorecem leitura mais clara do que em produção;
- o classificador não representa decisão real sobre artistas;
- filtros descritivos não alteram a parte de treino do modelo, que continua
  baseada no conjunto completo.

## 10. Conclusão

O dashboard final cumpre o papel esperado pela trilha:

- organiza a leitura do projeto;
- comunica os achados com clareza;
- preserva responsabilidade metodológica;
- e oferece uma interface mais madura para demonstração, exploração e discussão
  da base analítica do FlowCarreiras.

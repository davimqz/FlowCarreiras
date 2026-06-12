# Plano de Análise

## Estratégia principal

A entrega principal utiliza somente duas bases abertas e reais:

- **Mapa Cultural de Pernambuco:** perfis individuais com área artística/criativa declarada;
- **contempArt:** artistas em início de carreira e seus sinais de presença digital.

Essas bases não medem o uso real do FlowCarreiras. Elas permitem investigar problemas relacionados ao produto e apoiar decisões sobre perfil, portfólio, categorias, filtros e exposição justa.

## Pergunta central

Como dados reais disponíveis podem orientar o FlowCarreiras na organização profissional, construção de portfólio, estruturação de filtros e promoção de exposição mais justa para artistas?

## Objetivos

1. Identificar lacunas na apresentação profissional de perfis artísticos.
2. Definir áreas, tags e combinações úteis para cadastros, filtros, oportunidades e mentorias.
3. Investigar dependência de redes sociais e ausência de portfólio independente.
4. Medir desigualdade de visibilidade digital sem tratá-la como qualidade artística.
5. Identificar perfis com volume registrado relevante e baixa visibilidade.
6. Traduzir resultados reais em decisões e hipóteses para o FlowCarreiras.

## Perguntas respondidas pelo Mapa Cultural PE

### Perfil e onboarding

1. Qual percentual possui descrição, área, tags, funções e subáreas preenchidas?
2. Quais campos apresentam mais ausências e deveriam receber maior apoio no onboarding?
3. Quantos perfis podem ser considerados minimamente estruturados?
4. Quantos artistas declaram atuação em mais de uma área?
5. Perfis multidisciplinares apresentam estrutura de preenchimento diferente?

### Taxonomia e filtros

1. Quais áreas, tags, funções e subáreas aparecem com maior frequência?
2. Quais áreas e tags aparecem juntas com maior frequência?
3. Quais categorias devem compor filtros iniciais do aplicativo?
4. Quais categorias menos frequentes precisam ser preservadas para evitar exclusão?
5. Quais grupos de perfis surgem quando artistas são aproximados por áreas e tags compartilhadas?
6. Quais perfis conectam diferentes grupos de interesses culturais?

### Atualização do perfil

1. Como os registros se distribuem por ano de criação?
2. Quantos perfis foram atualizados após sua criação?
3. Perfis atualizados possuem maior preenchimento?

## Perguntas respondidas pelo contempArt

### Portfólio e presença digital

1. Quantos artistas possuem website informado?
2. Quantos possuem somente Instagram como presença digital identificável?
3. Quantos não possuem Instagram nem website informados?
4. Artistas com maior volume de imagens ou publicações possuem mais frequentemente website?

### Exposição e visibilidade

1. Como seguidores, publicações, imagens, curtidas e comentários estão distribuídos?
2. A quantidade de publicações ou imagens registradas possui relação com seguidores?
3. Artistas com volumes registrados semelhantes apresentam visibilidades muito diferentes?
4. Existem artistas com volume registrado acima da mediana e visibilidade abaixo da mediana?
5. Quanto da visibilidade está concentrado nos 10% e 20% maiores perfis?
6. Quais variáveis apresentam maior associação com o nível de visibilidade?

### Agrupamento por similaridade

1. Quais grupos de artistas surgem a partir de características digitais semelhantes?
2. Como os grupos diferem em publicações, imagens, presença de website, tipo de conta e contas seguidas?
3. Os grupos encontrados também apresentam diferenças de visibilidade?
4. Existem perfis isolados ou muito diferentes dos demais?

## Perguntas gerais sobre o FlowCarreiras apoiadas pelos dados reais

1. Quais campos e orientações devem ser priorizados no cadastro progressivo?
2. O perfil deve permitir múltiplas áreas e combinações de tags?
3. Quais categorias devem orientar filtros de obras, oportunidades e mentorias?
4. Qual parcela dos artistas poderia se beneficiar de um portfólio público independente?
5. Por que descoberta e recomendação não devem usar somente seguidores ou engajamento?
6. Quais critérios observáveis podem ajudar a destacar perfis estruturados com baixa visibilidade?
7. Que indicadores deverão ser coletados futuramente para avaliar o aplicativo de verdade?

## EDA principal

- qualidade e lacunas de preenchimento dos perfis;
- diversidade e coocorrência de áreas e tags;
- rede de similaridade por interesses declarados;
- presença digital e disponibilidade de website;
- distribuições e valores atípicos das métricas digitais;
- concentração e desigualdade de visibilidade;
- quadrantes entre volume registrado e visibilidade.
- agrupamentos de artistas por características digitais.

## Agrupamento não supervisionado recomendado

### Rede de interesses - Mapa Cultural PE

Construir uma rede em que cada nó representa um perfil e cada conexão representa similaridade entre áreas e tags declaradas. A similaridade pode ser calculada por Jaccard ou cosseno após transformar os termos em variáveis binárias.

Para manter o gráfico legível, devem ser exibidas somente conexões acima de um limite de similaridade e, quando necessário, uma amostra ou os maiores componentes da rede.

Pergunta: quais comunidades de interesses podem orientar filtros, recomendações e descoberta dentro do FlowCarreiras?

### Agrupamento de características - contempArt

Padronizar características como `posts_count`, `img_count`, `following_count`, presença de website, tipo de conta e características médias das imagens. Aplicar agrupamento, como K-Means ou hierárquico, e usar PCA apenas para projeção visual.

`follower_count` não deve definir os grupos principais; ele pode ser usado depois para caracterizar como a visibilidade se distribui entre os grupos.

Pergunta: artistas com características digitais semelhantes recebem níveis diferentes de visibilidade?

## Modelagem principal com dados reais

### Regressão

Investigar a relação entre `posts_count`, `img_count`, `following_count` e `follower_count`, usando transformações logarítmicas quando necessário.

Pergunta: volume registrado e atividade digital explicam parte da variação de seguidores?

### Classificação

Classificar `nivel_visibilidade` com variáveis disponíveis antes de observar o número de seguidores, como publicações, imagens, website, tipo de conta e quantidade de contas seguidas.

Pergunta: quais características observáveis estão associadas a níveis distintos de visibilidade?

O alvo e as variáveis devem ser revisados para evitar vazamento de dados. Os modelos são exploratórios e não devem ser usados para ranquear artistas.

## Perguntas futuras que exigem dados reais do aplicativo

1. Onde usuários abandonam o onboarding?
2. Quanto tempo levam para publicar a primeira obra?
3. Quais oportunidades geram visualizações, acessos e candidaturas?
4. Quais matches resultam em mentorias concluídas?
5. A descoberta interna distribui exposição de forma equilibrada?
6. O uso contínuo melhora organização, portfólio e acesso a oportunidades?

Essas perguntas fazem parte do plano de instrumentação e não serão respondidas com dados inventados.

## Regra de interpretação

- Cada resultado deve indicar a fonte utilizada.
- As duas bases reais serão analisadas separadamente.
- Ausência de campo é sinal observável, não prova de uma dor individual.
- Seguidores, curtidas e comentários não representam talento ou qualidade.
- Associações e modelos não demonstram causalidade.
- Grupos calculados representam similaridade nas colunas selecionadas, não amizades, identidade artística ou qualidade.

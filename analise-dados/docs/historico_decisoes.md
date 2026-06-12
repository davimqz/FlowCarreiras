# Histórico de Decisões

## 12 de junho de 2026

- Reorientadas as perguntas de análise para as dores dos artistas e as funcionalidades reais do FlowCarreiras.
- Escolas, gênero e distribuição geográfica deixaram de ser objetivos centrais e passaram a ser apenas recortes auxiliares.
- Definidas perguntas futuras e indicadores próprios para avaliar onboarding, portfólio, mentoria, oportunidades e exposição justa.
- Registrado que as bases externas apoiam hipóteses de produto, mas não medem diretamente a eficácia do aplicativo.
- Criada uma matriz de cobertura separando respostas diretas, aproximações e perguntas dependentes de dados próprios.
- Criado um plano de instrumentação para avaliar o funcionamento real do FlowCarreiras.
- Corrigida a interpretação de `img_count`: volume de imagens incluídas no contempArt, não produção artística total.
- Criadas variáveis derivadas orientadas ao produto para apoiar onboarding, portfólio, presença digital e exposição justa.

## 9 de junho de 2026

- Ampliado o recorte do Mapa Cultural PE de 100 para 1.000 registros paginados.
- Adicionados objetivos da comunicação visual e justificativas dos gráficos planejados.
- Documentada a justificativa do pré-processamento por tipo de variável.
- Limitado o recorte do Mapa Cultural PE a 1.000 agentes individuais (`type=EQ(1)`), alinhando a base ao foco atual do FlowCarreiras.
- Refinado o recorte para perfis individuais com área artística/criativa declarada, removendo registros evidentemente administrativos ou de teste.
- Corrigida a taxa de engajamento para permanecer ausente quando likes ou comentários não estiverem disponíveis.
- Implementada validação e conversão das datas do Mapa Cultural PE.
- Implementadas variáveis derivadas em arquivos enriquecidos separados das bases limpas.

## 8 de junho de 2026

- Substituído o antigo CSV de pontos de cultura por uma extração reproduzível da API de agentes do Mapa Cultural de Pernambuco.
- Mantido o dataset alemão, com sua origem identificada como o projeto científico aberto contempArt.
- Removida a classificação das bases como simuladas.
- Definidas perguntas e métricas separadas para cada dataset.
- Registrado que as bases são complementares no tema, mas não são integradas.
- Centralizados dados, scripts e documentação na pasta `analise-dados`.

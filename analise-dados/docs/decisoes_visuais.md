# Decisões Visuais

## Narrativa obrigatória com dados reais

1. **Organizar:** quais informações faltam para os perfis se apresentarem profissionalmente?
2. **Estruturar:** quais áreas e tags devem apoiar cadastro, filtros e recomendações?
3. **Independência digital:** quantos artistas possuem website, somente Instagram ou nenhuma presença informada?
4. **Exposição justa:** volume registrado e alcance caminham juntos?
5. **Aplicar ao produto:** quais decisões e indicadores futuros surgem dos resultados?

## Visualizações obrigatórias

| Visualização | Fonte | Pergunta apoiada | Justificativa perceptual e visual |
| --- | --- | --- | --- |
| Barras de cobertura por campo | Mapa Cultural PE | Quais etapas do perfil precisam de maior apoio? | O comprimento em uma escala comum permite comparar percentuais com precisão e identificar rapidamente os campos mais incompletos. |
| Barras ordenadas de áreas, tags e funções | Mapa Cultural PE | Quais categorias devem orientar filtros? | A ordenação reduz o esforço de busca visual e torna claras diferenças entre muitas categorias, sendo mais legível que gráficos de setores. |
| Histograma de quantidade de áreas | Mapa Cultural PE | O perfil deve apoiar atuação multidisciplinar? | Agrupa uma variável numérica em intervalos e revela concentração, dispersão e valores extremos sem resumir tudo em uma única média. |
| Matriz de coocorrência | Mapa Cultural PE | Quais categorias aparecem juntas? | Usa posição e intensidade de cor para permitir comparação simultânea de muitos pares e destacar combinações recorrentes. |
| Barras ou cartões de presença digital | contempArt | Quantos possuem website, somente Instagram ou nenhum dos dois? | Barras favorecem comparação precisa entre categorias; cartões destacam poucos indicadores principais com hierarquia visual clara. |
| Histogramas em escala logarítmica | contempArt | Como métricas digitais se distribuem? | A escala logarítmica reduz a distorção causada por poucos valores muito altos e permite perceber a forma da distribuição da maioria dos artistas. |
| Scatterplots com tendência | contempArt | Volume registrado possui relação com visibilidade? | A posição em dois eixos evidencia associação, dispersão, grupos e valores atípicos; a linha resume a tendência sem esconder a variação individual. |
| Quadrantes volume-visibilidade | contempArt | Quem apresenta volume alto e visibilidade baixa? | Os quadrantes transformam duas medidas em perfis interpretáveis e tornam imediatamente visível o grupo relacionado à proposta de exposição justa. |
| Curva ou barras de concentração | contempArt | Quanto do alcance fica concentrado em poucos perfis? | A visualização evidencia desigualdade acumulada e comunica melhor a concentração do que apenas médias ou totais. |
| Heatmap de correlação | contempArt | Quais variáveis apresentam associação? | A matriz organiza todas as associações em uma visão compacta; a intensidade de cor facilita reconhecer padrões fortes, fracos e negativos. |

## Visualizações adicionais recomendadas

Estas visualizações incorporam a sugestão docente de agrupamento. Elas aprofundam a análise, mas não substituem os gráficos principais.

| Visualização | Fonte | Pergunta apoiada | Justificativa perceptual e visual |
| --- | --- | --- | --- |
| Rede simplificada de similaridade por interesses | Mapa Cultural PE | Quais comunidades surgem a partir de áreas e tags compartilhadas? | Proximidade, conexões e cores ajudam a perceber comunidades e perfis conectores; filtros e limites evitam sobrecarga visual. |
| Ego network | Mapa Cultural PE | Quais perfis são mais semelhantes a um artista selecionado? | Reduz a complexidade da rede completa e concentra a atenção no perfil central, seus vizinhos e interesses compartilhados. |
| Dispersão PCA colorida por cluster | contempArt | Quais grupos de características semelhantes podem ser identificados? | Projeta múltiplas features em dois eixos para apoiar a inspeção visual de separação e sobreposição entre grupos calculados. |
| Barras ou heatmap do perfil médio dos clusters | contempArt | Como os grupos encontrados diferem entre si? | Permite comparar várias características dos grupos em escala comum, revelando quais features realmente os diferenciam. |

## Organização recomendada do dashboard

### Página 1 - Organização profissional

- cobertura dos campos;
- perfil minimamente estruturado;
- diversidade de atuação;
- implicações para onboarding.

### Página 2 - Categorias do ecossistema

- áreas, tags, funções e subáreas;
- combinações frequentes;
- rede simplificada de interesses compartilhados;
- implicações para filtros, oportunidades e mentoria.

### Página 3 - Presença e portfólio digital

- Instagram e website;
- volume de posts e imagens;
- implicações para portfólio público.

### Página 4 - Visibilidade e exposição justa

- distribuição de seguidores e engajamento;
- relações entre volume e alcance;
- concentração e quadrantes.
- grupos de características semelhantes e seus perfis médios.

### Página 5 - Aplicação e limites

- decisões apoiadas;
- limitações;
- indicadores futuros do aplicativo.

## Princípios

- Identificar claramente a fonte e o universo válido.
- Manter as duas bases reais separadas.
- Não criar ranking de artistas.
- Não tratar popularidade como qualidade.
- Não apresentar hipóteses sobre o aplicativo como impacto comprovado.
- Explicar que as conexões representam similaridade calculada, não relações sociais reais.
- Evitar redes com excesso de nós e conexões; usar filtros, amostras ou componentes principais.
- Priorizar posição e comprimento para comparações precisas; usar cor como apoio, não como único meio de comunicação.
- Manter escalas, títulos, unidades, fontes e universo válido explícitos.
- Evitar gráficos de setores quando houver muitas categorias ou diferenças pequenas.
- Usar contraste e hierarquia para destacar a pergunta principal sem transformar popularidade em ranking.

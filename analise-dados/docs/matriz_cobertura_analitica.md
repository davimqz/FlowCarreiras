# Matriz de Cobertura Analítica

## Parecer

Com as bases atuais é possível realizar uma análise completa para a disciplina de Análise e Visualização de Dados, conectando EDA, regressão, classificação e dashboard a decisões do FlowCarreiras.

Entretanto, as bases externas não permitem avaliar completamente o funcionamento ou impacto do aplicativo. Elas sustentam hipóteses e decisões iniciais. Resultados reais sobre onboarding, portfólio, mentoria, oportunidades e exposição justa dependem de dados próprios de uso.

## Níveis de evidência

- **Direto:** a pergunta pode ser respondida com uma variável observada no dataset.
- **Aproximação:** existe um indicador relacionado, mas ele não comprova diretamente a dor ou comportamento.
- **Dados próprios:** a pergunta somente poderá ser respondida com eventos e registros do FlowCarreiras.

## Cobertura por eixo

| Eixo do produto | Pergunta | Fonte | Nível | Decisão apoiada |
| --- | --- | --- | --- | --- |
| Organização profissional | Quais informações dos perfis estão mais ausentes? | Mapa Cultural PE | Direto | Priorizar etapas e orientações do onboarding |
| Organização profissional | O artista possui uma carreira profissionalmente estruturada? | Mapa Cultural PE | Aproximação | Ausência ou presença de campos não comprova organização profissional |
| Perfil artístico | Artistas atuam em múltiplas áreas? | Mapa Cultural PE | Direto | Permitir múltiplas áreas e sugestões relacionadas |
| Taxonomia | Quais áreas e tags aparecem juntas? | Mapa Cultural PE | Direto | Estruturar categorias, filtros e sugestões iniciais |
| Portfólio | Quantos não possuem website informado? | contempArt | Direto | Sustentar a utilidade de um portfólio público próprio |
| Portfólio | Quantos necessitam de ajuda para montar portfólio? | contempArt | Aproximação | Website ausente não comprova dificuldade para montar portfólio |
| Presença digital | Quantos possuem somente Instagram informado? | contempArt | Direto | Investigar dependência de uma única plataforma informada |
| Exposição justa | Alcance acompanha o volume registrado de publicações e imagens? | contempArt | Direto | Demonstrar limites de recomendações baseadas em popularidade |
| Exposição justa | Artistas com menor alcance possuem maior qualidade? | Nenhuma | Dados próprios/qualitativos | Qualidade artística não é medida pelas bases |
| Mentoria | Quais áreas podem orientar cobertura inicial de mentores? | Mapa Cultural PE | Aproximação | Frequência de áreas não equivale à procura por mentoria |
| Mentoria | Quais necessidades possuem maior demanda e menor oferta? | FlowCarreiras | Dados próprios | Comparar `tagsNecessidade` e `tagsExpertise` |
| Mentoria | Matches compatíveis resultam em mentorias concluídas? | FlowCarreiras | Dados próprios | Avaliar eficácia do sistema de compatibilidade |
| Oportunidades | Quais áreas devem aparecer nos filtros iniciais? | Mapa Cultural PE | Aproximação | Frequência de áreas ajuda a estruturar cobertura inicial |
| Oportunidades | Artistas encontram e acessam oportunidades relevantes? | FlowCarreiras | Dados próprios | Medir visualizações, notificações e acessos externos |
| Exposição justa | A fila distribui aparições de forma equilibrada? | FlowCarreiras | Dados próprios parcialmente disponíveis | Usar `fila_descoberta_log` e instrumentar visualizações |
| Evolução profissional | O FlowCarreiras melhora a carreira dos usuários? | FlowCarreiras + pesquisa com usuários | Dados próprios/qualitativos | Exige acompanhamento longitudinal e feedback dos artistas |

## Cobertura dos entregáveis de AVD

| Entregável | Viabilidade com os dados atuais | Direção recomendada |
| --- | --- | --- |
| EDA | Alta | Lacunas de perfil, taxonomia, presença digital e desigualdade de alcance |
| Análise multivariada | Alta no contempArt | Relações entre seguidores, posts, imagens registradas, likes e comentários |
| Regressão | Alta no contempArt | Explicar tendências de alcance, sem interpretar como causalidade ou mérito |
| Classificação | Média/alta | Classificar quadrantes de presença/visibilidade ou nível de visibilidade derivado |
| Dashboard | Alta | Narrativa organizada por decisões do FlowCarreiras |
| Avaliação real do aplicativo | Baixa atualmente | Requer instrumentação e dados próprios |

## Conclusão

O projeto deve apresentar duas camadas:

1. **Análise externa para orientar o produto:** usa Mapa Cultural PE e contempArt para investigar organização de perfis, taxonomia, presença digital e desigualdade de alcance.
2. **Plano de avaliação do FlowCarreiras:** define quais dados próprios deverão ser coletados para medir onboarding, portfólio, mentoria, oportunidades, exposição justa e evolução profissional.

Essa estrutura permite uma entrega completa e coerente sem afirmar que dados externos representam usuários reais ou comprovam o impacto do aplicativo.

## Extensões opcionais

O repositório completo do contempArt também disponibiliza:

- `images.csv`, com uma linha por imagem e, para parte delas, data, likes e comentários;
- `edgelist.csv`, com a rede direcionada de seguidores do Instagram.

Esses arquivos poderiam aprofundar análises temporais e de concentração em redes sociais. Contudo, não são necessários para cobrir os requisitos principais da disciplina e aumentariam consideravelmente a complexidade. A recomendação é concluir primeiro a análise com `artists.csv` e considerar essas extensões somente se houver tempo.

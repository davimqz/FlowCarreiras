# Métricas

As métricas foram escolhidas para apoiar decisões do FlowCarreiras. Escolas, gênero e região podem ser usados como recortes auxiliares na EDA, mas não são objetivos centrais da análise.

## Mapa Cultural de Pernambuco

| Métrica | Cálculo | Relação com o FlowCarreiras |
| --- | --- | --- |
| Total de perfis artísticos individuais | Contagem de `id` únicos após aplicação do critério artístico/criativo | Dimensionar o recorte analisado |
| Cobertura da descrição | Percentual com `descricao_curta` preenchida | Indicar necessidade de apoio para criação de bio profissional |
| Cobertura de tags | Percentual com `termos_tags` preenchida | Avaliar disponibilidade de informações para recomendações e matches |
| Cobertura de funções | Percentual com `termos_funcoes` preenchida | Avaliar clareza sobre atuação profissional |
| Perfil minimamente estruturado | Percentual com descrição, ao menos uma área e tags ou funções preenchidas | Aproximar a necessidade de onboarding guiado |
| Lacunas por campo | Percentual ausente em descrição, tags, funções e subáreas | Priorizar etapas e mensagens do onboarding |
| Frequência de áreas, tags e funções | Explosão das listas e contagem de ocorrências | Informar a taxonomia inicial de categorias, filtros, mentorias e oportunidades |
| Diversidade de atuação | Média, mediana e distribuição de `quantidade_areas` | Decidir como representar artistas multidisciplinares |
| Combinações recorrentes | Contagem de pares de áreas ou área-tag dentro do mesmo perfil | Apoiar sugestões e compatibilidade |
| Atualização do perfil | Tempo desde `data_atualizacao` e percentual atualizado após criação | Investigar necessidade de lembretes para manter o perfil atual |

## contempArt

| Métrica | Cálculo | Relação com o FlowCarreiras |
| --- | --- | --- |
| Total de artistas | Contagem de `artist_id` únicos | Dimensionar o recorte analisado |
| Cobertura de Instagram | Percentual com `instagram_handle` preenchido | Medir presença em rede social |
| Cobertura de website | Percentual com `website` preenchido | Aproximar disponibilidade de portfólio independente |
| Somente Instagram informado | Instagram preenchido e website ausente | Identificar presença digital concentrada em uma plataforma social |
| Sem presença digital informada | Instagram e website ausentes | Identificar perfis que poderiam se beneficiar de portfólio próprio |
| Volume registrado | Distribuição de `posts_count` e `img_count` | Representar publicações do Instagram e imagens incluídas no dataset, sem tratar como produção artística total |
| Visibilidade digital | Distribuição e mediana de `follower_count` | Medir alcance sem tratá-lo como qualidade |
| Engajamento calculável | Distribuição de `taxa_engajamento` somente entre registros válidos | Analisar interação sem imputar ausentes |
| Volume registrado alto e visibilidade baixa | `posts_count` ou `img_count` acima da mediana e seguidores abaixo da mediana | Identificar público relacionado à exposição justa |
| Concentração de visibilidade | Participação dos 10% e 20% maiores perfis no total de seguidores e interações | Demonstrar desigualdade de alcance |
| Relação volume-visibilidade | Correlação e regressão entre posts/imagens registradas e seguidores/engajamento | Verificar limites de rankings por popularidade |
| Dispersão entre volumes semelhantes | Variação de seguidores entre faixas semelhantes de posts ou imagens registradas | Mostrar que volumes registrados semelhantes podem receber alcance desigual |

## Indicadores futuros do próprio FlowCarreiras

| Eixo | Indicadores |
| --- | --- |
| Onboarding e organização | Taxa de conclusão, abandono por etapa, percentual de completude, tempo até completar perfil |
| Portfólio | Tempo até primeira obra, obras publicadas por artista, percentual com portfólio público, compartilhamentos |
| Mentoria | Demanda e oferta por tag, compatibilidade média, matches iniciados, mentorias concluídas |
| Oportunidades | Visualizações, acessos externos, notificações abertas e oportunidades acessadas por área/tag |
| Exposição justa | Distribuição de aparições na fila, artistas distintos exibidos, concentração de visualizações e descoberta de perfis novos |
| Retenção e evolução | Retorno à plataforma, atualização do perfil e crescimento de obras organizadas ao longo do tempo |

Os indicadores futuros devem ser acompanhados por um plano de instrumentação, pois nem todos são registrados atualmente pelo aplicativo.

## Regras de cálculo

- Informar o universo válido e os dados ausentes em cada resultado.
- Não usar seguidores, likes ou comentários como medida de talento ou qualidade artística.
- Não misturar métricas das duas bases em um único cálculo.
- Tratar métricas externas como evidências para decisões de produto, não como medição de impacto do aplicativo.

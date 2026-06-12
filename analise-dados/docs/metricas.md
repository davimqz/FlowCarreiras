# Métricas

Todas as métricas obrigatórias são calculadas exclusivamente com as duas bases reais.

## Mapa Cultural de Pernambuco

| Métrica | Cálculo | Decisão apoiada |
| --- | --- | --- |
| Total de perfis individuais | Contagem de `id` únicos | Dimensionar o recorte |
| Cobertura de descrição | Percentual com `descricao_curta` preenchida | Priorizar apoio para bio |
| Cobertura de áreas | Percentual com `termos_areas` preenchida | Avaliar capacidade de categorização |
| Cobertura de tags | Percentual com `termos_tags` preenchida | Avaliar informação para filtros e recomendações |
| Cobertura de funções | Percentual com `termos_funcoes` preenchida | Avaliar clareza profissional |
| Cobertura de subáreas | Percentual com `termos_subareas` preenchida | Avaliar detalhamento |
| Perfil minimamente estruturado | Descrição e área preenchidas, além de tags ou funções | Aproximar necessidade de onboarding |
| Diversidade de atuação | Distribuição de `quantidade_areas` | Apoiar perfis multidisciplinares |
| Frequência de categorias | Contagem após explodir áreas, tags e funções | Estruturar taxonomia e filtros |
| Coocorrência | Contagem de pares de áreas e tags no mesmo perfil | Apoiar sugestões relacionadas |
| Similaridade por interesses | Índice de Jaccard ou cosseno entre conjuntos de áreas e tags | Identificar grupos e recomendações possíveis |
| Comunidades de interesses | Comunidades detectadas na rede após limitar conexões fracas | Explorar agrupamentos culturais |
| Perfis conectores | Centralidade de intermediação ou grau na rede filtrada | Identificar combinações que conectam interesses distintos |
| Atualização posterior | Percentual de `atualizacao_posterior = true` | Investigar manutenção do perfil |

## contempArt

| Métrica | Cálculo | Decisão apoiada |
| --- | --- | --- |
| Total de artistas | Contagem de `artist_id` únicos | Dimensionar o recorte |
| Cobertura de Instagram | Percentual de `possui_instagram = true` | Medir presença em rede social |
| Cobertura de website | Percentual de `possui_website = true` | Aproximar presença de portfólio independente |
| Somente Instagram informado | Instagram preenchido e website ausente | Investigar dependência de plataforma social |
| Sem presença digital informada | Instagram e website ausentes | Identificar possível utilidade do portfólio público |
| Distribuição de volume | Distribuições de `posts_count` e `img_count` | Entender atividade e volume registrado |
| Distribuição de visibilidade | Distribuição de `follower_count` | Entender desigualdade de alcance |
| Engajamento calculável | Distribuição de `taxa_engajamento` apenas em registros válidos | Analisar interação sem imputar ausentes |
| Alto volume e baixa visibilidade | Quadrante com volume acima e seguidores abaixo das medianas | Apoiar exposição justa |
| Concentração de visibilidade | Participação dos 10% e 20% maiores no total de seguidores | Demonstrar concentração |
| Relação volume-visibilidade | Correlação e regressão entre volume registrado e seguidores | Testar limites de recomendações por popularidade |
| Nível de visibilidade | Faixas derivadas dos percentis válidos de seguidores | Apoiar classificação exploratória |
| Qualidade do agrupamento | Silhouette score e comparação entre quantidades de clusters | Selecionar agrupamento sem definir grupos arbitrariamente |
| Perfil dos clusters | Médias, medianas e proporções por cluster | Interpretar características de cada grupo |

## Métricas futuras do aplicativo

Estas métricas não fazem parte dos resultados atuais:

- abandono e conclusão do onboarding;
- tempo até primeira obra;
- visualizações e compartilhamentos de portfólio;
- acessos e candidaturas a oportunidades;
- demanda, oferta e conclusão de mentorias;
- distribuição interna de aparições e visualizações;
- retenção e evolução profissional.

## Regras

- Informar o universo válido e os ausentes em cada cálculo.
- Não substituir ausentes por zero sem justificativa.
- Não misturar registros das duas bases reais.
- Não usar popularidade como medida de mérito.
- Não apresentar métricas futuras ou opcionais como resultados observados.
- Não interpretar centralidade como importância artística.
- Documentar as features utilizadas e padronizá-las antes do agrupamento.

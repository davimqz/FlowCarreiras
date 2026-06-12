# Resultados dos Modelos - Task 03

## Escopo

Os modelos utilizam exclusivamente o dataset real contempArt. Eles investigam sinais digitais disponíveis, mas não medem qualidade artística, sucesso profissional ou impacto do FlowCarreiras.

## Regressões simples

As variáveis foram transformadas com `log1p` devido à forte assimetria. As métricas foram calculadas em um conjunto de teste com 25% dos registros válidos.

| Relação | Registros | Coeficiente | R² teste | MAE teste | RMSE teste |
| --- | ---: | ---: | ---: | ---: | ---: |
| Posts x seguidores | 359 | 0,358 | 0,148 | 0,685 | 0,909 |
| Imagens registradas x seguidores | 359 | 0,237 | 0,024 | 0,758 | 0,972 |
| Seguidores x média de curtidas | 200 | 0,660 | 0,416 | 0,456 | 0,620 |
| Posts x taxa de engajamento | 200 | -0,275 | 0,131 | 0,433 | 0,572 |

### Interpretação

- Posts apresentam associação positiva com seguidores, mas explicam somente 14,8% da variação observada no teste.
- A quantidade de imagens registradas explica muito pouco da variação de seguidores.
- Seguidores apresentam associação com curtidas, porém isso representa popularidade digital, não mérito artístico.
- O volume de posts apresenta associação negativa com a taxa de engajamento relativa.
- Os resíduos mostram grande variação entre artistas com volumes semelhantes.

## Classificação do nível de visibilidade

O alvo possui três classes derivadas dos percentis de seguidores: baixo, médio e alto. Seguidores, curtidas, comentários, taxa de engajamento e variáveis derivadas deles foram excluídos das features para evitar vazamento de dados.

O modelo principal é uma regressão logística multiclasse com imputação, padronização e codificação de categorias dentro do pipeline.

| Métrica | Resultado |
| --- | ---: |
| Registros válidos | 359 |
| Registros de teste | 90 |
| Acurácia baseline | 0,333 |
| Acurácia do modelo | 0,589 |
| Precisão macro | 0,607 |
| Recall macro | 0,589 |
| F1 macro | 0,589 |
| ROC AUC macro | 0,776 |
| Average Precision macro | 0,666 |

### Desempenho por classe

| Classe | Precisão | Recall | F1-score |
| --- | ---: | ---: | ---: |
| Baixo | 0,62 | 0,77 | 0,69 |
| Médio | 0,44 | 0,47 | 0,45 |
| Alto | 0,76 | 0,53 | 0,63 |

As features com maior importância por permutação foram `following_count`, `posts_count`, `avg_width`, `is_business` e `school`. Essas associações não demonstram causalidade.

## Implicações para o FlowCarreiras

1. Publicar mais está associado a maior visibilidade, mas não explica sozinho o alcance.
2. Artistas com volumes semelhantes podem apresentar resultados digitais muito diferentes.
3. O nível intermediário de visibilidade foi o mais difícil de classificar.
4. Os modelos não devem ser utilizados para ordenar artistas, decidir mérito ou limitar oportunidades.
5. A descoberta deve combinar interesses, organização profissional e distribuição justa de exposição.

## Limitações

- O contempArt representa artistas ligados a escolas de arte alemãs, com coleta entre 2018 e 2020.
- O dataset não representa usuários do FlowCarreiras.
- Seguidores e engajamento não representam qualidade ou evolução profissional.
- As regressões são exploratórias e não demonstram causalidade.
- O nível de visibilidade é uma variável derivada do próprio dataset.
- O desempenho do classificador não autoriza uso em decisões reais sobre artistas.

## Entregáveis relacionados

- `notebooks/05_regressao.ipynb`
- `notebooks/06_classificacao.ipynb`
- `models/regressao/regressao_posts_seguidores.joblib`
- `models/classificacao/classificador_nivel_visibilidade.joblib`

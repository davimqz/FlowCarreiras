# Plano de Instrumentação do FlowCarreiras

## Objetivo

Definir os dados próprios necessários para avaliar se o FlowCarreiras resolve as dores dos artistas. Este documento é um plano analítico; não implica implementação imediata no aplicativo.

## Dados que o aplicativo já registra

| Área | Dados disponíveis atualmente |
| --- | --- |
| Perfil e onboarding | Área principal, cidade, bio, foto, links, tags de necessidade, completude, conclusão e status das etapas |
| Portfólio | Obras, tags, status e data de publicação |
| Mentoria | Mentor, artista, status, data de criação, encerramento e mensagens |
| Oportunidades | Tipo, área, tags, prazo e notificações geradas/lidas |
| Exposição justa | Registros da fila de descoberta por perfil, obra e data/hora |

## Eventos que ainda precisam ser registrados

| Evento sugerido | Campos mínimos | Pergunta respondida |
| --- | --- | --- |
| `onboarding_etapa_iniciada` | perfil, etapa, data/hora | Onde o usuário inicia e abandona o onboarding? |
| `onboarding_etapa_concluida` | perfil, etapa, data/hora | Quanto tempo cada etapa leva? |
| `onboarding_etapa_pulada` | perfil, etapa, data/hora | Quais campos geram maior resistência? |
| `portfolio_visualizado` | perfil visualizado, visitante, origem, data/hora | O portfólio está sendo descoberto? |
| `portfolio_compartilhado` | perfil, canal/origem, data/hora | O artista utiliza a URL pública? |
| `obra_visualizada` | obra, artista, visitante, origem, data/hora | A exposição está distribuída entre obras e artistas? |
| `oportunidade_visualizada` | oportunidade, perfil, data/hora | Quais oportunidades despertam interesse? |
| `oportunidade_acesso_externo` | oportunidade, perfil, data/hora | Quais oportunidades geram ação? |
| `notificacao_aberta` | notificação, oportunidade, perfil, data/hora | As notificações ajudam o acesso? |
| `mentor_visualizado` | mentor, artista interessado, data/hora | Quais áreas possuem procura por orientação? |
| `mentoria_match_exibido` | mentor, artista, compatibilidade, data/hora | Matches mais compatíveis são vistos e escolhidos? |
| `feedback_mentoria` | mentoria, avaliação, comentário opcional | A mentoria foi útil? |

## Indicadores prioritários

### Organização e onboarding

- taxa de conclusão do onboarding;
- abandono e etapas puladas;
- tempo até atingir o nível mínimo de completude;
- evolução da completude após 7, 30 e 90 dias.

### Portfólio

- tempo até publicar a primeira obra;
- percentual de artistas com obra publicada;
- visualizações por portfólio e obra;
- compartilhamentos da URL pública;
- concentração de visualizações entre artistas.

### Mentoria

- demanda por `tagsNecessidade`;
- oferta por `tagsExpertise`;
- lacunas entre demanda e oferta;
- compatibilidade média dos matches;
- mentorias iniciadas e concluídas;
- avaliação após encerramento.

### Oportunidades

- cobertura de oportunidades por área e tag;
- notificações geradas, lidas e abertas;
- visualizações e acessos externos;
- tempo entre publicação e acesso;
- áreas com alta demanda e baixa oferta de oportunidades.

### Exposição justa

- aparições por artista na fila;
- artistas distintos exibidos por período;
- tempo sem exposição;
- concentração de visualizações;
- diferença de exposição entre perfis novos e antigos;
- parcela de artistas pouco visíveis externamente que recebem descoberta no aplicativo.

## Cuidados

- Coletar somente dados necessários e informar os usuários sobre seu uso.
- Evitar armazenar dados pessoais sensíveis sem necessidade.
- Utilizar identificadores internos nas análises.
- Não transformar exposição justa em ranking público.
- Combinar métricas quantitativas com entrevistas e feedback dos artistas.

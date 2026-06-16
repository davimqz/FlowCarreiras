# Dataset simulado de artistas do FlowCarreiras - Recife

## Finalidade

Este arquivo contém 1.000 perfis totalmente fictícios de artistas de Recife para testes, análises exploratórias, modelagem e prototipação do dashboard do FlowCarreiras. Nenhuma linha representa uma pessoa real.

## Base metodológica

- Baseado nas relações observadas nas 21 respostas da pesquisa `Pesquisa com Artistas Emergentes - Grupo 11`.
- As respostas completas foram reamostradas com reposição para preservar relações entre área, trajetória, dificuldades, divulgação, tecnologia, mentoria e intenção de uso.
- Campos operacionais do aplicativo foram simulados condicionalmente a essas respostas e aos campos existentes no FlowCarreiras.
- Todos os perfis foram fixados em `Recife/PE`, conforme o recorte solicitado.
- Semente de reprodução: `11032026`.

## Cuidados de interpretação

A pesquisa original possui somente 21 respostas e não é uma amostra probabilística da população artística do Recife. As 1.000 linhas ampliam um cenário para estudo e testes; elas não representam 1.000 pessoas reais e não devem ser usadas para estimativas oficiais sobre a população artística da cidade.

## Regras principais

- `percentual_completude_perfil` segue a regra do aplicativo: área 30, tags de necessidade 25, cidade 20, bio 10, foto 10 e links externos 5.
- Organização do portfólio influencia obras publicadas, links e dias sem atualização, sem determinar completamente esses campos.
- Experiência influencia tags de expertise e possibilidade de atuar como mentor.
- Dificuldades e apoios desejados alimentam `necessidades_identificadas_pesquisa`; somente parte delas vira `tags_necessidade`, simulando usuários que ainda não preencheram essa etapa.
- `pronto_para_oportunidades` considera completude, obras publicadas e apresentação do perfil.
- `risco_estagnacao` combina organização do portfólio, atividade no aplicativo, candidaturas e dificuldades profissionais.

## Arquivo

- `flowcarreiras_artistas_simulados_recife.csv`: uma linha por artista fictício, UTF-8 com BOM e listas separadas por `|`.

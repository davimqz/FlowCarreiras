# Módulo opcional de análise simulada

Este módulo utiliza `flowcarreiras_artistas_simulados_recife.csv` para explorar cenários de uso do aplicativo.

Ele não faz parte da análise principal, não é necessário para cumprir os requisitos e não deve ser apresentado como comportamento real dos usuários.

## Como remover

Apagar integralmente a pasta `data/external/`.

Nenhum notebook, modelo, documento ou dashboard obrigatório depende deste módulo.

## Perguntas opcionais

### Aderência e adoção

1. Quais perfis apresentam maior intenção de utilizar o FlowCarreiras?
2. Quais barreiras podem reduzir a adoção do aplicativo?
3. O uso predominante de celular exige uma experiência mais simples e leve?

### Onboarding e portfólio

1. Quais características estão associadas à conclusão do onboarding?
2. A completude do perfil está relacionada à publicação de obras?
3. Quais grupos passam mais tempo sem atualizar o portfólio?
4. Quais fatores estão associados a um portfólio público ativo?

### Oportunidades

1. Onde estão os gargalos entre visualizar, salvar e candidatar-se?
2. A dificuldade com editais está associada a menos candidaturas?
3. Quais perfis aparecem como prontos para oportunidades?

### Mentoria

1. Existe equilíbrio entre interesse em mentoria e disponibilidade de mentores?
2. Quais apoios são mais desejados?
3. Experiência está associada à disponibilidade para mentorar?

### Evolução

1. Quais fatores estão associados ao risco de estagnação?
2. Perfis ativos, completos e com obras apresentam menor risco?

## Gráficos opcionais

| Gráfico | Objetivo |
| --- | --- |
| Barras de dores e necessidades | Comparar prioridades dos potenciais usuários |
| Funil ativo → onboarding → portfólio → candidatura | Explorar gargalos hipotéticos do aplicativo |
| Histograma de completude | Observar distribuição dos perfis simulados |
| Scatterplot completude × obras publicadas | Investigar relação entre perfil e portfólio |
| Barras de interesse × oferta de mentoria | Explorar equilíbrio potencial |
| Funil oportunidades visualizadas → salvas → candidaturas | Explorar conversão hipotética |
| Barras de prontidão e risco | Comparar grupos que precisariam de apoio |
| Heatmap de correlação | Investigar relações entre métricas simuladas |

## Modelos opcionais

- regressão para quantidade de obras publicadas ou candidaturas;
- classificação de `pronto_para_oportunidades`;
- classificação de `risco_estagnacao`.

Esses modelos são apenas exercícios sobre cenários simulados. Os modelos obrigatórios do projeto utilizam o contempArt.

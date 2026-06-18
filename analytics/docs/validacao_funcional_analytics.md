# Validação Funcional da Solução Visual

**Projeto:** FlowCarreiras  
**Data da validação técnica:** 17 de junho de 2026  
**Escopo:** base analítica, notebooks, scripts, Docker e dashboard

---

## 1. Objetivo

Registrar uma checagem técnica da trilha analítica antes da apresentação final,
garantindo que a entrega possa ser reproduzida sem alterar a base de **400
perfis simulados**.

O foco desta validação é confirmar:

- integridade da base analítica;
- executabilidade dos notebooks;
- consistência dos scripts auxiliares;
- coerência mínima do dashboard e do ambiente.

## 2. Protocolo de validação

A validação foi organizada em quatro frentes:

1. **base processada**;
2. **artefatos analíticos**;
3. **camada de aplicação**;
4. **ambiente de execução**.

## 3. Validações realizadas

| Item | Verificação | Resultado |
|---|---|---|
| Base analítica | Leitura de `perfil_features.csv` | Aprovado |
| Dimensão | 400 linhas e 33 colunas | Aprovado |
| Identificadores | Sem `usuario_id` ou `perfil_id` duplicados | Aprovado |
| Valores ausentes | Nenhum valor nulo na tabela processada | Aprovado |
| Completude | Todos os valores entre 0 e 100 | Aprovado |
| Alvo de classificação | Somente valores 0 e 1 | Aprovado |
| Análise exploratória | Notebook executado e salvo sem erros | Aprovado |
| Regressão de engajamento | Notebook executado e salvo sem erros | Aprovado |
| Classificação da fila | Notebook executado e salvo sem erros | Aprovado |
| Scripts Python | Compilação de dashboard, extração e builds | Aprovado |
| Docker Compose | Configuração validada com `docker compose config` | Aprovado |

## 4. O que cada validação garante

### 4.1 Base analítica

As checagens de dimensão, duplicidade, nulos, domínio e consistência garantem
que a tabela analítica está apta para:

- visualização;
- segmentação;
- regressão;
- classificação.

### 4.2 Notebooks

A execução sem erro dos três notebooks confirma:

- dependências resolvidas;
- integridade básica da estrutura de colunas;
- aderência entre base, transformações e visualizações.

### 4.3 Scripts auxiliares

A compilação do dashboard e dos scripts de extração/build reduz o risco de:

- referências quebradas;
- erros sintáticos;
- divergência entre o conteúdo documentado e o pipeline real.

### 4.4 Docker e ambiente

A validação do `docker compose` assegura que a infraestrutura declarada está
coerente, ainda que a validação visual completa dependa do ambiente integrado em
execução.

## 5. Interatividade esperada no dashboard

O dashboard analítico deve permitir:

- filtros globais por área artística, cidade, completude e fila;
- leitura de KPIs e composição da base;
- exploração de distribuições com escala opcional;
- inspeção de correlação entre métricas;
- ajuste do número de clusters na segmentação;
- avaliação do classificador com limiar ajustável;
- acesso ao modo individual embutido no sistema.

## 6. Critérios de aceite antes da apresentação

Na máquina de demonstração, o grupo precisa confirmar:

1. `docker compose up --build` termina sem falhas;
2. o seed cria os perfis simulados esperados;
3. `/dashboard` abre a visão agregada;
4. `/metricas` abre a visão individual após login;
5. filtros e abas atualizam os gráficos corretamente;
6. o download da tabela analítica funciona;
7. não há cortes de texto ou sobreposição visual;
8. o conteúdo final registrado em documentação corresponde ao que aparece no dashboard.

## 7. Limites desta validação

Esta validação **não substitui** uma verificação completa do fluxo integrado em
produção. Ela confirma:

- arquivos;
- base;
- notebooks;
- sintaxe;
- configuração declarativa.

Ela **não garante sozinha**:

- experiência visual final no navegador de apresentação;
- latência do ambiente completo;
- comportamento do app com serviços externos ou dados reais.

## 8. Resultado

A trilha analítica está tecnicamente consistente e reproduzível. O que resta é
uma conferência integrada do ambiente em execução e a validação final da
apresentação visual no host onde a demonstração será feita.

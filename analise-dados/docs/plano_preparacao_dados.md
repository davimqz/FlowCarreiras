# Plano de Preparação dos Dados

Cada base possui um fluxo independente de extração e limpeza.

## Mapa Cultural de Pernambuco

1. Consultar a API com os campos e limites definidos.
2. Salvar a resposta integral em JSON, preservando metadados da extração.
3. Validar que a resposta contém uma lista de agentes.
4. Achatar os objetos `type`, `terms`, `createTimestamp` e `updateTimestamp`.
5. Converter listas de termos para texto separado por ` | `.
6. Remover duplicidades apenas pelo campo `id`.
7. Ordenar os registros por `id`.
8. Manter campos ausentes vazios, sem inventar ou imputar valores.
9. Exportar o resultado em CSV UTF-8.

Scripts:

- `src/extract/baixar_mapa_cultural_pe.py`
- `src/cleaning/limpar_mapa_cultural_pe.py`

## contempArt

1. Baixar `artists.csv` do repositório oficial no GitHub.
2. Ler o arquivo preservando os nomes originais das colunas.
3. Remover espaços no início e no fim dos campos textuais.
4. Remover duplicidades apenas pelo campo `artist_id`.
5. Ordenar os registros por `artist_id`.
6. Manter valores ausentes como ausentes.
7. Exportar o resultado em CSV UTF-8.

Scripts:

- `src/extract/baixar_contempart.py`
- `src/cleaning/limpar_contempart.py`

## Regras gerais

- Não unir os datasets.
- Não traduzir ou substituir categorias originais durante a limpeza.
- Não preencher dados ausentes com médias, zeros ou categorias inventadas.
- Preservar uma cópia bruta para auditoria e reprodução.
- Registrar fonte, parâmetros e data da extração.

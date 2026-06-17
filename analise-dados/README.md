# Análise de Dados - FlowCarreiras

Esta pasta concentra a preparação e a documentação dos dados usados pelo projeto.

## Datasets

O projeto utiliza três fontes de dados distintas para fundamentar as análises estratégicas, mapear dores do ecossistema e validar hipóteses de regras de negócio. Para cumprir os requisitos de rigor metodológico, cada base é tratada de forma estritamente independente e isolada em páginas específicas do painel, garantindo que os datasets não sejam fundidos ou comparados de forma direta e artificial:

- **Mapa Cultural de Pernambuco:** Recorte de perfis individuais com área artística e criativa declarada, extraídos da API pública estadual. Focado no diagnóstico de estruturação profissional e multidisciplinaridade regional.
- **contempArt:** Métricas digitais externas de artistas em início de carreira vinculados a 15 escolas de arte da Alemanha. Utilizado para investigar a assimetria de visibilidade algorítmica e portfólio digital externo.
- **Cenário simulado FlowCarreiras:** Conjunto de dados sintéticos contendo métricas de comportamento interno de utilizadores (completude de perfil, logs de atividade, total de obras e candidaturas). Utilizado exclusivamente para projetar o acompanhamento futuro de indicadores de sucesso e engajamento dentro do aplicativo.


A análise é orientada às decisões do FlowCarreiras: apoiar organização profissional e portfólio, estruturar categorias para mentoria e oportunidades e investigar desigualdade de visibilidade. Os dados externos geram hipóteses de produto; o impacto real do aplicativo deverá ser medido futuramente com dados próprios de uso.

## Estrutura

```text
data/raw/        Dados originais baixados das fontes públicas
data/processed/  Arquivos CSV limpos, enriquecidos e prontos para análise
data/external/   Conjunto de dados sintéticos e documentação de simulação
dashboard/       Script principal de inicialização e roteamento do painel
dashboard/views/ Scripts modulares contendo a lógica visual de cada página
docs/            Fontes, métricas e dicionários de variáveis
src/extract/     Scripts automatizados de extração
src/cleaning/    Scripts de limpeza e transformação de dados
```
## Para rodar o dashboard
```bash
streamlit run dashboard/app.py
```

## Reprodução

```bash
pip install -r analise-dados/requirements.txt

python analise-dados/src/extract/baixar_mapa_cultural_pe.py --total-registros 1000
python analise-dados/src/cleaning/limpar_mapa_cultural_pe.py

python analise-dados/src/extract/baixar_contempart.py
python analise-dados/src/cleaning/limpar_contempart.py
python analise-dados/src/features/criar_variaveis_derivadas.py
```

O download padrão do Mapa Cultural pagina a API até obter 1.000 perfis individuais que atendam ao critério artístico documentado em `docs/fontes_dados.md`.

Consulte também:

- `docs/matriz_cobertura_analitica.md`: mostra o que pode ser respondido diretamente, por aproximação ou somente com dados próprios.
- `docs/plano_instrumentacao.md`: define os eventos e indicadores futuros necessários para avaliar o aplicativo.

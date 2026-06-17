import streamlit as st

st.markdown("## Página 6 - Aplicação Prática no Produto e Limitações")
st.markdown(
    "Esta secção consolida as decisões estratégicas de engenharia e regras de negócio "
    "que a equipa do FlowCarreiras deve adotar com base nas evidências geradas pelos dados públicos e simulados."
)

st.divider()

st.subheader("Decisões de Engenharia de Produto Recomendadas")
st.text(
    "1. Flexibilidade de Cadastro: Permitir a seleção e persistência de múltiplas áreas artísticas \n"
    "   simultâneas nos perfis, respeitando o índice de 63.5% de multidisciplinaridade mapeado na base real.\n"
    "2. Gamificação do Onboarding: Adotar o preenchimento assistido e obrigatório para tags refinadas e \n"
    "   subáreas artísticas, impedindo perfis incompletos de avançarem sem estruturação profissional básica.\n"
    "3. Portfólio Independente: Desenvolver e priorizar a funcionalidade de uma página pública de portfólio \n"
    "   gerada internamente, provendo autonomia para os 42.6% de artistas dependentes de redes externas.\n"
    "4. Algoritmo de Exposição Justa: Estruturar os mecanismos de descoberta e busca baseando-se em tags de \n"
    "   interesse e volume de obras documentadas. O sistema não deve utilizar contagem de seguidores ou \n"
    "   curtidas de redes externas para ordenar ou ranquear os criativos na plataforma."
)

st.divider()

st.subheader("Limitações Estatísticas dos Dados Analisados")
st.text(
    "1. Base Territorial PE: O dataset do Mapa Cultural de Pernambuco exibe o mapeamento estrutural de \n"
    "   agentes reais, porém não possui metadados de dor, interações dentro de aplicações ou buscas por mentoria.\n"
    "2. Base de Audiência contempArt: O conjunto de dados de redes digitais reflete métricas de um ecossistema \n"
    "   específico ligado a escolas alemãs, não mapeando a realidade comportamental do mercado brasileiro.\n"
    "3. Cenário Simulado: O arquivo de simulação de uso do FlowCarreiras é um modelo puramente hipotético \n"
    "   e sintético, servindo estritamente como guia de visualização para arquiteturas futuras de logs."
)

st.divider()

st.subheader("Indicadores Críticos para Acompanhamento em Produção")
st.text(
    "1. Índice de Gini interno para monitorizar e combater a concentração de visualizações no catálogo.\n"
    "2. Percentual de utilizadores ativos que migraram do estado 'Incompleto' para 'Pronto para Oportunidades'.\n"
    "3. Taxa de rejeição ou abandono nas etapas específicas do fluxo de onboarding."
)
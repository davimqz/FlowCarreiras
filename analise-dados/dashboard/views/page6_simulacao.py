import streamlit as st
import plotly.express as px
import pandas as pd

if 'df_simulado' not in st.session_state:
    st.error("Dados não inicializados. Execute o dashboard a partir do arquivo app.py.")
    st.stop()

df_sim = st.session_state['df_simulado']

st.markdown("## Página 5 - Cenário simulado de uso do FlowCarreiras")

st.markdown(
    """
    <div style="background-color: #1c1d26; padding: 15px; border-left: 5px solid #00cc96; border-radius: 4px; margin-bottom: 20px;">
        <span style="color: #ffffff; font-weight: bold; display: block;">Base de dados consultada</span>
        <span style="color: #00cc96; font-size: 0.9em;">Dataset: Base interna simulada (Métricas de log geradas pelo fluxo do aplicativo)</span>
    </div>
    """, 
    unsafe_allow_html=True
)

st.subheader("Indicadores gerais de operação futura")

total_usuarios = len(df_sim)
if total_usuarios > 0:
    taxa_onboarding = (df_sim['onboarding_concluido'].sum() / total_usuarios) * 100
    media_obras = df_sim['total_obras'].mean()
    taxa_prontidao = (df_sim['pronto_para_oportunidades'].sum() / total_usuarios) * 100
else:
    taxa_onboarding = taxa_prontidao = media_obras = 0.0

col1, col2, col3 = st.columns(3)
col1.metric(label="Utilizadores simulados na base", value=f"{total_usuarios:,}")
col2.metric(label="Taxa de conclusão do onboarding", value=f"{taxa_onboarding:.1f}%")
col3.metric(label="Artistas prontos para oportunidades", value=f"{taxa_prontidao:.1f}%")

st.divider()

st.subheader("Como a recorrência de uso da plataforma impacta as chances de o artista se candidatar?")
st.markdown(
    "Justificativa visual: O gráfico de barras comparativo isola a atividade e contrasta o roxo da marca "
    "com a inatividade, evidenciando o valor gerado pela retenção de utilizadores."
)

if total_usuarios > 0:
    df_uso = df_sim.copy()
    df_uso['estado_atividade'] = df_uso['ativo_no_app'].map({True: 'Utilizador ativo', False: 'Utilizador inativo'})
    
    df_agrupado_uso = df_uso.groupby('estado_atividade', observed=False)['candidaturas_oportunidades_90d'].mean().reset_index()
    
    fig_uso = px.bar(
        df_agrupado_uso,
        x='estado_atividade',
        y='candidaturas_oportunidades_90d',
        text='candidaturas_oportunidades_90d',
        labels={
            'estado_atividade': 'Estado de atividade no aplicativo',
            'candidaturas_oportunidades_90d': 'Média de candidaturas (Últimos 90 dias)'
        },
        color='estado_atividade',
        color_discrete_map={'Utilizador ativo': '#ab63fa', 'Utilizador inativo': '#1c1d26'}
    )
    fig_uso.update_traces(texttemplate='%{text:.2f}', textposition='outside')
    fig_uso.update_layout(height=400, showlegend=False)
    st.plotly_chart(fig_uso, use_container_width=True)

st.divider()

st.subheader("Como o acervo de portfólio está distribuído entre os níveis de visibilidade alcançados?")
st.markdown(
    "Justificativa visual: O gráfico de caixa (box plot) resume a distribuição de uma variável numérica "
    "contínua dividida por categorias de exposição, evidenciando medianas, dispersão e outliers de obras cadastradas."
)

if total_usuarios > 0:
    fig_sim_box = px.box(
        df_sim,
        x='nivel_visibilidade_no_app',
        y='total_obras',
        color='nivel_visibilidade_no_app',
        labels={
            'nivel_visibilidade_no_app': 'Nível de visibilidade no aplicativo',
            'total_obras': 'Total de obras no portfólio'
        },
        color_discrete_sequence=['#ab63fa', '#19d3f3', '#00cc96']
    )
    fig_sim_box.update_layout(height=400, showlegend=False)
    st.plotly_chart(fig_sim_box, use_container_width=True)

st.divider()

st.subheader("Qual é a concentração da integridade os dados cadastrados pelos utilizadores?")
st.markdown(
    "Justificativa visual: O histograma fatia a escala contínua de integridade do perfil em intervalos regulares, "
    "utilizando a cor primária da marca para expor a volumetria absoluta de preenchimento cadastral."
)

if total_usuarios > 0:
    fig_sim_hist = px.histogram(
        df_sim,
        x='percentual_completude_perfil',
        nbins=20,
        labels={'percentual_completude_perfil': 'Percentual de completude do perfil (%)'},
        color_discrete_sequence=['#ab63fa']
    )
    fig_sim_hist.update_layout(
        yaxis_title="Quantidade de artistas",
        xaxis=dict(range=[0, 105]),
        height=350
    )
    st.plotly_chart(fig_sim_hist, use_container_width=True)

st.divider()

st.subheader("Interpretação analítica do cenário futuro")
st.text(
    "1. Retorno sobre atividade: Os dados agregados comprovam que artistas que mantêm uma rotina ativa \n"
    "   no aplicativo possuem uma média de candidaturas significativamente superior aos inativos, validando o app.\n"
    "2. Diagnóstico de abandono: O mapeamento do histograma permite identificar os pontos de evasão \n"
    "   antes que os perfis atinjam os níveis ideais de maturidade profissional."
)
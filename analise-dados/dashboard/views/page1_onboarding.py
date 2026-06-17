import streamlit as st
import plotly.express as px
import pandas as pd

if 'df_mapa_filtrado' not in st.session_state:
    st.error("Dados não inicializados. Execute o dashboard a partir do arquivo app.py.")
    st.stop()

df_mapa = st.session_state['df_mapa_filtrado']

st.markdown("## Página 1 - Organização profissional e diagnóstico de campos")

st.markdown(
    """
    <div style="background-color: #1c1d26; padding: 15px; border-left: 5px solid #ab63fa; border-radius: 4px; margin-bottom: 20px;">
        <span style="color: #ffffff; font-weight: bold; display: block;">Base de dados consultada</span>
        <span style="color: #a3a8b4; font-size: 0.9em;">Dataset: Mapa Cultural de Pernambuco (Recorte territorial de agentes culturais)</span>
    </div>
    """, 
    unsafe_allow_html=True
)

st.subheader("Indicadores de estruturação do ecossistema")

total_agentes = len(df_mapa)
if total_agentes > 0:
    pct_estruturado = (df_mapa['perfil_minimamente_estruturado'].sum() / total_agentes) * 100
    pct_multidisciplinar = (df_mapa['perfil_multidisciplinar'].sum() / total_agentes) * 100
    media_areas = df_mapa['quantidade_areas'].mean()
else:
    pct_estruturado = pct_multidisciplinar = media_areas = 0.0

col1, col2, col3 = st.columns(3)
col1.metric(label="Total de agentes analisados", value=f"{total_agentes:,}")
col2.metric(label="Perfis estruturados profissionalmente", value=f"{pct_estruturado:.1f}%")
col3.metric(label="Agentes com atuação multidisciplinar", value=f"{pct_multidisciplinar:.1f}%")

st.divider()

st.subheader("Quais etapas do perfil precisam de maior apoio no onboarding de utilizadores?")
st.markdown(
    "Justificativa visual: O comprimento das barras horizontais em uma escala comum de 0 a 100% "
    "permite comparar percentuais com precisão e identificar os campos mais negligenciados."
)

if total_agentes > 0:
    campos = {
        "Descrição curta": df_mapa['possui_descricao'].mean() * 100,
        "Tags de interesse": df_mapa['possui_tags'].mean() * 100,
        "Funções culturais": df_mapa['possui_funcoes'].mean() * 100,
        "Subáreas artísticas": df_mapa['possui_subareas'].mean() * 100
    }
    
    df_cobertura = pd.DataFrame(list(campos.items()), columns=['Campo', 'Percentual']).sort_values(by='Percentual', ascending=True)
    
    fig_cobertura = px.bar(
        df_cobertura,
        x='Percentual',
        y='Campo',
        orientation='h',
        text='Percentual',
        labels={'Percentual': 'Preenchimento (%)'},
        color='Percentual',
        color_continuous_scale=['#1c1d26', '#ab63fa']
    )
    
    fig_cobertura.update_traces(texttemplate='%{text:.1f}%', textposition='outside')
    fig_cobertura.update_layout(
        xaxis=dict(range=[0, 110]), 
        yaxis_title=None,
        coloraxis_showscale=False,
        height=300
    )
    st.plotly_chart(fig_cobertura, use_container_width=True)

st.divider()

st.subheader("Como a diversidade de atuação e a multidisciplinaridade se manifestam no ecossistema?")
st.markdown(
    "Justificativa visual: O histograma agrupa a variável discreta de quantidade de áreas, "
    "expondo graficamente a concentração da atuação múltipla sem resumir o comportamento a uma média."
)

if total_agentes > 0:
    fig_histograma = px.histogram(
        df_mapa,
        x='quantidade_areas',
        nbins=int(df_mapa['quantidade_areas'].max() - df_mapa['quantidade_areas'].min() + 1),
        labels={'quantidade_areas': 'Quantidade de áreas declaradas', 'count': 'Número de agentes'},
        color_discrete_sequence=['#ab63fa']
    )
    
    fig_histograma.update_layout(
        yaxis_title="Quantidade de artistas",
        xaxis=dict(tickmode='linear', tick0=1, dtick=1),
        height=300
    )
    st.plotly_chart(fig_histograma, use_container_width=True)

st.divider()

st.subheader("Implicações práticas para o modelo de carreira")
st.text(
    "1. Demanda por preenchimento guiado: O ecossistema aponta que dados subjetivos e refinados possuem \n"
    "   baixíssima taxa de preenchimento espontâneo, sugerindo a necessidade de fluxos assistidos no produto.\n"
    "2. Flexibilidade cadastral: Como a maioria dos agentes atua em múltiplos nichos simultaneamente, \n"
    "   restringir o cadastro a um único rótulo profissional geraria uma limitação artificial prejudicial."
)
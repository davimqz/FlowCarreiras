import streamlit as st
import plotly.express as px
import pandas as pd

if 'df_contempart_filtrado' not in st.session_state:
    st.error("Dados não inicializados. Execute o dashboard a partir do arquivo app.py.")
    st.stop()

df_art = st.session_state['df_contempart_filtrado']

st.markdown("## Página 3 - Presença e portfólio digital externo")

st.markdown(
    """
    <div style="background-color: #1c1d26; padding: 15px; border-left: 5px solid #ab63fa; border-radius: 4px; margin-bottom: 20px;">
        <span style="color: #ffffff; font-weight: bold; display: block;">Base de dados consultada</span>
        <span style="color: #a3a8b4; font-size: 0.9em;">Dataset: contempArt (Métricas digitais de visibilidade e canais externos)</span>
    </div>
    """, 
    unsafe_allow_html=True
)

st.subheader("Indicadores de independência em canais digitais")

total_artistas = len(df_art)
if total_artistas > 0:
    pct_instagram = (df_art['possui_instagram'].sum() / total_artistas) * 100
    pct_website = (df_art['possui_website'].sum() / total_artistas) * 100
    pct_exclusivo_insta = (df_art['somente_instagram_informado'].sum() / total_artistas) * 100
else:
    pct_instagram = pct_website = pct_exclusivo_insta = 0.0

col1, col2, col3 = st.columns(3)
col1.metric(label="Presença ativa no Instagram", value=f"{pct_instagram:.1f}%")
col2.metric(label="Autonomia com website próprio", value=f"{pct_website:.1f}%")
col3.metric(label="Dependência exclusiva de redes sociais", value=f"{pct_exclusivo_insta:.1f}%")

st.divider()

st.subheader("Como os agentes distribuem a sua presença pública nos canais digitais?")
st.markdown(
    "Justificativa visual: O gráfico de barras horizontais ordenadas permite comparar o volume absoluto "
    "de criativos em cada modalidade, mapeando o nível de centralização da exposição digital."
)

if total_artistas > 0:
    contagem_canais = {
        "Apenas Instagram": df_art['somente_instagram_informado'].sum(),
        "Instagram e website": ((df_art['possui_instagram'] == True) & (df_art['possui_website'] == True)).sum(),
        "Apenas website": ((df_art['possui_instagram'] == False) & (df_art['possui_website'] == True)).sum(),
        "Sem presença informada": df_art['sem_presenca_digital_informada'].sum()
    }
    
    df_canais = pd.DataFrame(list(contagem_canais.items()), columns=['Canal', 'Quantidade']).sort_values(by='Quantidade', ascending=True)
    
    fig_canais = px.bar(
        df_canais,
        x='Quantidade',
        y='Canal',
        orientation='h',
        text='Quantidade',
        color_discrete_sequence=['#ab63fa'],
        labels={'Quantidade': 'Volume de perfis'}
    )
    fig_canais.update_traces(textposition='outside')
    fig_canais.update_layout(yaxis_title=None, height=300)
    st.plotly_chart(fig_canais, use_container_width=True)

st.divider()

st.subheader("Qual é a relação entre a atividade social externa e a documentação física do acervo?")
st.markdown(
    "Justificativa visual: O gráfico de dispersão com opacidade reduzida expõe o cruzamento de dois eixos contínuos, "
    "sendo ideal para identificar se maior volume de postagens digitais gera maior volume de obras registradas."
)

if total_artistas > 0:
    fig_scatter = px.scatter(
        df_art,
        x='posts_count',
        y='img_count',
        labels={'posts_count': 'Volume de postagens externas', 'img_count': 'Obras documentadas no portfólio'},
        color_discrete_sequence=['#ab63fa'],
        opacity=0.5
    )
    fig_scatter.update_layout(height=400)
    st.plotly_chart(fig_scatter, use_container_width=True)

st.divider()

st.subheader("Diagnóstico de autonomia digital")
st.text(
    "1. Vulnerabilidade algorítmica: A dependência exclusiva de redes sociais externas por uma parcela expressiva \n"
    "   dos agentes evidencia o risco de perda de alcance devido a mudanças imprevistas em plataformas de terceiros.\n"
    "2. Desconexão de portfólio: A dispersão demonstra que alta atividade em redes de entretenimento não se traduz \n"
    "   em catalogação efetiva de acervo, reforçando a importância de um espaço dedicado para portfólios estruturados."
)
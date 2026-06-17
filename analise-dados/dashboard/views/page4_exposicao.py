import streamlit as st
import plotly.express as px
import pandas as pd
import numpy as np

if 'df_contempart_filtrado' not in st.session_state:
    st.error("Dados não inicializados. Execute o dashboard a partir do arquivo app.py.")
    st.stop()

df_art = st.session_state['df_contempart_filtrado']

st.markdown("## Página 4 - Visibilidade e exposição justa")

st.markdown(
    """
    <div style="background-color: #1c1d26; padding: 15px; border-left: 5px solid #ab63fa; border-radius: 4px; margin-bottom: 20px;">
        <span style="color: #ffffff; font-weight: bold; display: block;">Base de dados consultada</span>
        <span style="color: #a3a8b4; font-size: 0.9em;">Dataset: contempArt (Métricas digitais de visibilidade e canais externos)</span>
    </div>
    """, 
    unsafe_allow_html=True
)

st.subheader("Indicadores de desigualdade de alcance digital")

total_artistas = len(df_art)
if total_artistas > 0 and 'follower_count' in df_art.columns:
    seguidores_ordenados = df_art['follower_count'].dropna().sort_values(ascending=False)
    total_seguidores = seguidores_ordenados.sum()
    
    top_10_corte = int(np.ceil(0.10 * len(seguidores_ordenados)))
    pct_top10 = (seguidores_ordenados.head(top_10_corte).sum() / total_seguidores) * 100 if total_seguidores > 0 else 0.0
    
    mediana_seguidores = df_art['follower_count'].median()
    total_estagnados = df_art['risco_estagnacao'].sum()
else:
    pct_top10 = mediana_seguidores = total_estagnados = 0.0

col1, col2, col3 = st.columns(3)
col1.metric(label="Mediana de seguidores externos", value=f"{mediana_seguidores:,.0f}")
col2.metric(label="Concentração de alcance nos 10% maiores", value=f"{pct_top10:.1f}%")
col3.metric(label="Criativos com alto volume e baixa visibilidade", value=f"{total_estagnados}")

st.divider()

st.subheader("Qual é o nível de assimetria na distribuição de alcance do ecossistema?")
st.markdown(
    "Justificativa visual: A transformação para escala logarítmica achata os valores extremos de grandes contas, "
    "permitindo enxergar com clareza o formato e a distribuição real da maioria esmagadora dos perfis menores."
)

if total_artistas > 0 and df_art['follower_count'].notna().sum() > 0:
    df_log = df_art.copy()
    df_log['follower_count_log'] = np.log10(df_log['follower_count'] + 1)
    
    fig_hist_log = px.histogram(
        df_log,
        x='follower_count_log',
        labels={'follower_count_log': 'Seguidores externos (Escala Log10)', 'count': 'Frequência de perfis'},
        color_discrete_sequence=['#ab63fa']
    )
    fig_hist_log.update_layout(yaxis_title="Quantidade de artistas", height=300)
    st.plotly_chart(fig_hist_log, use_container_width=True)

st.divider()

st.subheader("Como se comportam os perfis no cruzamento entre volume de acervo documentado e alcance?")
st.markdown(
    "Justificativa visual: A divisão do espaço bivariado com linhas pontilhadas baseadas nas medianas "
    "isola visualmente o quadrante de interesse, contrastando o roxo da marca com as demais categorias."
)

if total_artistas > 0:
    med_img = df_art['img_count'].median()
    med_fol = df_art['follower_count'].median()
    
    df_quadrantes = df_art.copy()
    
    def rotular_quadrante(row):
        if row['img_count'] >= med_img and row['follower_count'] >= med_fol:
            return 'Alto volume / Alta visibilidade'
        elif row['img_count'] >= med_img and row['follower_count'] < med_fol:
            return 'Alto volume / Baixa visibilidade (Foco FlowCarreiras)'
        elif row['img_count'] < med_img and row['follower_count'] >= med_fol:
            return 'Baixo volume / Alta visibilidade'
        else:
            return 'Baixo volume / Baixa visibilidade'
            
    df_quadrantes['Quadrante'] = df_quadrantes.apply(rotular_quadrante, axis=1)
    
    fig_quad = px.scatter(
        df_quadrantes,
        x='img_count',
        y='follower_count',
        color='Quadrante',
        labels={'img_count': 'Volume de acervo catalogado', 'follower_count': 'Seguidores externos'},
        color_discrete_map={
            'Alto volume / Alta visibilidade': '#00cc96',
            'Alto volume / Baixa visibilidade (Foco FlowCarreiras)': '#ab63fa',
            'Baixo volume / Alta visibilidade': '#19d3f3',
            'Baixo volume / Baixa visibilidade': '#ef553b'
        },
        opacity=0.7
    )
    fig_quad.add_hline(y=med_fol, line_dash="dash", line_color="gray")
    fig_quad.add_vline(x=med_img, line_dash="dash", line_color="gray")
    fig_quad.update_layout(height=450)
    st.plotly_chart(fig_quad, use_container_width=True)

st.divider()

st.subheader("Análise de oportunidade de exposição")
st.text(
    "1. Falha de distribuição orgânica: Quase metade do alcance total acumulado está concentrada em apenas \n"
    "   10% das contas, demonstrando que depender de redes abertas perpetua um cenário de exclusão crônica.\n"
    "2. Isolamento de talentos: O quadrante destacado em roxo revela criativos com consistência e volume \n"
    "   de acervo consolidados, mas subexpostos pelo algoritmo comercial, legitimando a proposta de exposição justa."
)
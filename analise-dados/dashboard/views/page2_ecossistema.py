import streamlit as st
import plotly.express as px
import pandas as pd

if 'df_mapa_filtrado' not in st.session_state:
    st.error("Dados não inicializados. Execute o dashboard a partir do arquivo app.py.")
    st.stop()

df_mapa = st.session_state['df_mapa_filtrado']

st.markdown("## Página 2 - Categorias do ecossistema e associações")

st.markdown(
    """
    <div style="background-color: #1c1d26; padding: 15px; border-left: 5px solid #ab63fa; border-radius: 4px; margin-bottom: 20px;">
        <span style="color: #ffffff; font-weight: bold; display: block;">Base de dados consultada</span>
        <span style="color: #a3a8b4; font-size: 0.9em;">Dataset: Mapa Cultural de Pernambuco (Recorte territorial de agentes culturais)</span>
    </div>
    """, 
    unsafe_allow_html=True
)

def expandir_coluna_multivalor(df, coluna):
    serie_expandida = df[coluna].dropna().astype(str).str.split(r'\s*\|\s*')
    todos_termos = [termo.strip() for sublista in serie_expandida for termo in sublista if termo.strip()]
    return pd.Series(todos_termos)

st.subheader("Quais são as categorias e especialidades mais recorrentes entre os agentes registrados?")
st.markdown(
    "Justificativa visual: A ordenação decrescente reduz o esforço de busca visual e torna nítidas "
    "as diferenças de densidade entre os segmentos do ecossistema."
)

if len(df_mapa) > 0:
    col_graf1, col_graf2 = st.columns(2)
    
    with col_graf1:
        st.markdown("**Segmentos artísticos mais frequentes**")
        areas_expandidas = expandir_coluna_multivalor(df_mapa, 'termos_areas')
        if not areas_expandidas.empty:
            df_areas = areas_expandidas.value_counts().reset_index()
            df_areas.columns = ['Área', 'Contagem']
            
            fig_areas = px.bar(
                df_areas.head(10),
                x='Contagem',
                y='Área',
                orientation='h',
                color_discrete_sequence=['#ab63fa'],
                labels={'Contagem': 'Volume de registros'}
            )
            fig_areas.update_layout(yaxis_title=None, height=350)
            st.plotly_chart(fig_areas, use_container_width=True)
            
    with col_graf2:
        st.markdown("**Especialidades e palavras-chave mais comuns**")
        tags_expandidas = expandir_coluna_multivalor(df_mapa, 'termos_tags')
        if not tags_expandidas.empty:
            df_tags = tags_expandidas.value_counts().reset_index()
            df_tags.columns = ['Tag', 'Contagem']
            
            fig_tags = px.bar(
                df_tags.head(10),
                x='Contagem',
                y='Tag',
                orientation='h',
                color_discrete_sequence=['#ab63fa'],
                labels={'Contagem': 'Volume de ocorrências'}
            )
            fig_tags.update_layout(yaxis_title=None, height=350)
            st.plotly_chart(fig_tags, use_container_width=True)

st.divider()

st.subheader("Quais nichos e categorias de atuação costumam aparecer juntas nos perfis?")
st.markdown(
    "Justificativa visual: A matriz cartesiana de calor correlaciona a interseção de termos recorrentes, "
    "destacando agrupamentos e competências combinadas sem poluir a interface."
)

if len(df_mapa) > 0:
    areas_expandidas = expandir_coluna_multivalor(df_mapa, 'termos_areas')
    if not areas_expandidas.empty:
        top_areas = areas_expandidas.value_counts().head(6).index.tolist()
        matrix = pd.DataFrame(0, index=top_areas, columns=top_areas)
        
        for _, row in df_mapa['termos_areas'].dropna().astype(str).to_frame().iterrows():
            lista_areas = [a.strip() for a in row['termos_areas'].split('|') if a.strip() in top_areas]
            for i in range(len(lista_areas)):
                for j in range(len(lista_areas)):
                    matrix.loc[lista_areas[i], lista_areas[j]] += 1
                    
        fig_heatmap = px.imshow(
            matrix,
            text_auto=True,
            labels=dict(x="Categoria", y="Categoria", color="Conexões"),
            color_continuous_scale=['#1c1d26', '#ab63fa', '#19d3f3']
        )
        fig_heatmap.update_layout(height=400)
        st.plotly_chart(fig_heatmap, use_container_width=True)

st.divider()

st.subheader("Observações sobre o arranjo produtivo")
st.text(
    "1. Clusters de cooperação: Nota-se uma forte associação de competências técnicas com gestão cultural, \n"
    "   sinalizando que o artista local frequentemente acumula funções administrativas por necessidade do setor.\n"
    "2. Risco de isolamento: Segmentos com menor volume relativo podem sofrer invisibilidade caso as buscas \n"
    "   não considerem a árvore de termos correlacionados mapeada nesta matriz."
)
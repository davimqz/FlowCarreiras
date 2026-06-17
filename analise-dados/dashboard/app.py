import streamlit as st
import pandas as pd
import os
import re

st.set_page_config(
    page_title="FlowCarreiras - Painel de análise estratégica",
    layout="wide",
    initial_sidebar_state="expanded"
)

@st.cache_data
def load_data():
    base_path = os.path.dirname(os.path.abspath(__file__))
    mapa_pe_path = os.path.normpath(os.path.join(base_path, "../data/processed/mapa_cultural_pe_agentes_enriquecido.csv"))
    contempart_path = os.path.normpath(os.path.join(base_path, "../data/processed/contempart_artists_enriquecido.csv"))
    simulado_path = os.path.normpath(os.path.join(base_path, "../data/external/flowcarreiras_artistas_simulados_recife.csv"))
    
    df_mapa = pd.read_csv(mapa_pe_path)
    df_contempart = pd.read_csv(contempart_path)
    df_simulado = pd.read_csv(simulado_path)
    
    med_img = df_contempart['img_count'].median()
    med_fol = df_contempart['follower_count'].median()
    df_contempart['risco_estagnacao'] = (df_contempart['img_count'] >= med_img) & (df_contempart['follower_count'] < med_fol)
    
    return df_mapa, df_contempart, df_simulado

try:
    df_mapa, df_contempart, df_simulado = load_data()
except Exception as e:
    st.error(f"Erro ao carregar as bases de dados. Verifique os caminhos: {e}")
    st.stop()

st.sidebar.markdown("### Nota metodológica")
st.sidebar.warning(
    "Este painel apresenta uma análise de dados analítica baseada em conjuntos de dados públicos "
    "extraídos da internet e uma seção simulada. Não possui integração direta com a aplicação real do FlowCarreiras, "
    "servindo como suporte para compreensão de dores e estatísticas do ecossistema cultural."
)

st.sidebar.title("Filtros avançados")

st.sidebar.subheader("Ecossistema: Mapa Cultural PE")
mapeamento_areas = {
    "Música": ["Música", "Ópera", "Banda", "Cantor"],
    "Audiovisual": ["Audiovisual", "Cinema", "Vídeo", "Fotografia"],
    "Artes Visuais": ["Artes Visuais", "Pintura", "Escultura", "Grafite"],
    "Teatro e dança": ["Teatro", "Dança", "Circo", "Artes Cênicas"],
    "Literatura": ["Literatura", "Poesia", "Escrita", "Livro"],
    "Produção cultural": ["Produção Cultural", "Gestão Cultural", "Patrimônio"],
    "Design e artesanato": ["Design", "Artesanato", "Moda", "Arquitetura"]
}

macro_areas = list(mapeamento_areas.keys())
areas_selecionadas = st.sidebar.multiselect("Categorias artísticas principais", options=macro_areas)

perfil_estruturado_filtro = st.sidebar.checkbox("Apenas perfis estruturados profissionalmente")

st.sidebar.subheader("Visibilidade digital: contempArt")
risco_estagnacao = st.sidebar.checkbox("Apenas agentes em risco de estagnação")
filtro_presenca_web = st.sidebar.selectbox("Filtro de presença web", ["Todos", "Com website", "Sem website"])

df_mapa_filtrado = df_mapa.copy()
df_contempart_filtrado = df_contempart.copy()

if areas_selecionadas:
    termos_busca = []
    for area in areas_selecionadas:
        termos_busca.extend(mapeamento_areas[area])
    regex_busca = "|".join([re.escape(termo) for termo in termos_busca])
    df_mapa_filtrado = df_mapa_filtrado[df_mapa_filtrado['termos_areas'].dropna().str.contains(regex_busca, case=False, regex=True)]

if perfil_estruturado_filtro:
    df_mapa_filtrado = df_mapa_filtrado[df_mapa_filtrado['perfil_minimamente_estruturado'] == True]

if risco_estagnacao:
    df_contempart_filtrado = df_contempart_filtrado[df_contempart_filtrado['risco_estagnacao'] == True]

if filtro_presenca_web == "Com website":
    df_contempart_filtrado = df_contempart_filtrado[df_contempart_filtrado['possui_website'] == True]
elif filtro_presenca_web == "Sem website":
    df_contempart_filtrado = df_contempart_filtrado[df_contempart_filtrado['possui_website'] == False]

st.session_state['df_mapa_filtrado'] = df_mapa_filtrado
st.session_state['df_contempart_filtrado'] = df_contempart_filtrado
st.session_state['df_simulado'] = df_simulado

st.sidebar.divider()
st.sidebar.markdown("### Navegação")

paginas = {
    "1. Organização profissional": "page1_onboarding.py",
    "2. Categorias do ecossistema": "page2_ecossistema.py",
    "3. Presença & portfólio digital": "page3_presenca.py",
    "4. Visibilidade & exposição justa": "page4_exposicao.py",
    "5. Cenário simulado FlowCarreiras": "page6_simulacao.py",
    "6. Aplicação no produto & limites": "page5_decisoes.py"
}

opcao_navegacao = st.sidebar.radio("Ir para a seção:", list(paginas.keys()))

base_path_views = os.path.dirname(os.path.abspath(__file__))
nome_arquivo_visao = paginas[opcao_navegacao]
script_pagina = os.path.normpath(os.path.join(base_path_views, "views", nome_arquivo_visao))

if os.path.exists(script_pagina):
    exec(open(script_pagina, encoding="utf-8").read())
else:
    st.error(f"O arquivo não foi encontrado no diretório: {script_pagina}")
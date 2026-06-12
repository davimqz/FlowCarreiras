# Dicionário de Dados

## Mapa Cultural de Pernambuco

Arquivo tratado: `data/processed/mapa_cultural_pe_agentes.csv`

| Coluna | Tipo | Descrição |
| --- | --- | --- |
| `id` | Inteiro | Identificador único do agente na plataforma |
| `nome` | Texto | Nome informado pelo agente |
| `descricao_curta` | Texto | Descrição resumida do agente |
| `tipo_id` | Inteiro | Identificador do tipo de agente; neste recorte, sempre `1` |
| `tipo_nome` | Texto | Nome do tipo de agente; neste recorte, sempre `Individual` |
| `termos_tags` | Texto multivalor | Tags associadas ao agente, separadas por ` | ` |
| `termos_areas` | Texto multivalor | Áreas culturais associadas ao agente |
| `termos_funcoes` | Texto multivalor | Funções culturais informadas |
| `termos_etnias` | Texto multivalor | Termos de etnia informados |
| `termos_subareas` | Texto multivalor | Subáreas culturais informadas |
| `data_criacao` | Data/hora | Data de criação do registro |
| `data_atualizacao` | Data/hora | Data da atualização mais recente |
| `possui_descricao` | Booleano derivado | Indica se `descricao_curta` está preenchida |
| `possui_tags` | Booleano derivado | Indica se o perfil possui tags informadas |
| `possui_funcoes` | Booleano derivado | Indica se o perfil possui funções informadas |
| `possui_subareas` | Booleano derivado | Indica se o perfil possui subáreas informadas |
| `quantidade_areas` | Inteiro derivado | Quantidade de áreas culturais associadas ao agente |
| `perfil_multidisciplinar` | Booleano derivado | Indica se o perfil possui mais de uma área declarada |
| `perfil_minimamente_estruturado` | Booleano derivado | Indica descrição e área preenchidas, além de tags ou funções |
| `ano_criacao` | Inteiro derivado | Ano extraído de `data_criacao` |
| `atualizacao_posterior` | Booleano derivado | Indica atualização posterior à criação |

## contempArt

Arquivo tratado: `data/processed/contempart_artists.csv`

| Coluna | Tipo | Descrição |
| --- | --- | --- |
| `artist_id` | Texto | Identificador único do artista |
| `full_name` | Texto | Nome completo |
| `school` | Texto | Escola de arte associada |
| `east_german` | Booleano | Indica se a escola está na antiga Alemanha Oriental |
| `professor_class` | Texto | Professor ou classe/ateliê associado |
| `gender` | Texto | Gênero registrado na fonte |
| `country_iso3` | Texto | Código ISO 3166-1 alfa-3 do país |
| `continent` | Texto | Continente |
| `region` | Texto | Região geográfica usada pela fonte |
| `instagram_handle` | Texto | Identificador público no Instagram |
| `instagram_private` | Booleano | Indica se o perfil era privado |
| `instagram_private_allowed` | Booleano | Permissão registrada para perfil privado |
| `is_business` | Booleano | Indica perfil comercial |
| `is_private` | Booleano | Indica perfil privado |
| `follower_count` | Número | Quantidade de seguidores |
| `following_count` | Número | Quantidade de contas seguidas |
| `posts_count` | Número | Quantidade de publicações |
| `website` | URL | Site informado |
| `img_count` | Inteiro | Quantidade de imagens do artista na base |
| `avg_likes` | Número | Média de curtidas das imagens com dados disponíveis |
| `avg_comments` | Número | Média de comentários das imagens com dados disponíveis |
| `avg_file_size` | Número | Tamanho médio dos arquivos de imagem, em bytes |
| `avg_width` | Número | Largura média das imagens, em pixels |
| `avg_height` | Número | Altura média das imagens, em pixels |
| `avg_aspect_ratio` | Número | Proporção média entre largura e altura |
| `possui_instagram` | Booleano derivado | Indica se o identificador do Instagram está preenchido |
| `possui_website` | Booleano derivado | Indica se o website está preenchido |
| `somente_instagram_informado` | Booleano derivado | Indica Instagram preenchido e website ausente |
| `sem_presenca_digital_informada` | Booleano derivado | Indica Instagram e website ausentes |
| `taxa_engajamento` | Número derivado | Percentual calculado por `(avg_likes + avg_comments) / follower_count * 100` somente quando os três valores estão disponíveis e seguidores é maior que zero |
| `metricas_engajamento_disponiveis` | Booleano derivado | Indica se a taxa de engajamento pode ser calculada |
| `nivel_visibilidade` | Categoria derivada | Faixa baixa, média ou alta baseada nos percentis de seguidores |
| `quadrante_imagens_visibilidade` | Categoria derivada | Combinação entre `img_count` e seguidores acima ou abaixo de suas medianas válidas |

# Plano de Análise

## Direcionamento

A análise deve apoiar decisões do FlowCarreiras, uma plataforma voltada à organização profissional, construção de portfólio, exposição justa, mentoria e acesso a oportunidades para artistas emergentes.

Os datasets externos não representam usuários reais do aplicativo e não medem diretamente dores como dificuldade de conseguir mentoria ou acessar editais. Eles serão usados para investigar sinais observáveis relacionados a essas dores e orientar hipóteses de produto.

As duas bases serão analisadas separadamente:

- **Mapa Cultural de Pernambuco:** ajuda a estudar como artistas individuais estruturam sua apresentação profissional por meio de descrição, áreas e tags.
- **contempArt:** ajuda a estudar presença digital, volume de publicações e imagens registradas e desigualdade de visibilidade entre artistas em início de carreira.

## Objetivos da comunicação visual

1. Mostrar quantos artistas possuem informações suficientes para se apresentar profissionalmente e quantos poderiam se beneficiar de um onboarding guiado.
2. Identificar quais áreas e tags devem compor a estrutura inicial de oportunidades, mentorias e filtros, sem excluir áreas menos frequentes.
3. Evidenciar artistas com maior volume de publicações ou imagens registradas, mas pouca visibilidade digital, público relacionado à proposta de exposição justa.
4. Demonstrar por que quantidade de seguidores, curtidas ou publicações não deve ser usada isoladamente para definir relevância artística.
5. Transformar os achados em decisões e hipóteses testáveis para funcionalidades do aplicativo.
6. Definir indicadores futuros para avaliar se o FlowCarreiras realmente melhora organização, descoberta, mentoria e acesso a oportunidades.

## Eixos de análise

### 1. Organização profissional e portfólio

Dor relacionada: artistas produzem, mas encontram dificuldade para organizar e apresentar profissionalmente seus trabalhos.

#### Mapa Cultural de Pernambuco

Unidade de análise: um perfil individual que atende ao critério artístico/criativo documentado.

Perguntas:

1. Quantos perfis possuem uma apresentação profissional minimamente estruturada, considerando descrição, área e tags ou funções preenchidas?
2. Quais informações profissionais ficam ausentes com maior frequência e deveriam receber mais apoio durante o onboarding?
3. Quantos artistas atuam em múltiplas áreas, indicando que o FlowCarreiras não deve limitar o perfil a uma única forma de atuação?
4. Quais áreas, subáreas e tags devem compor a taxonomia inicial dos cadastros, filtros e recomendações do aplicativo?
5. Existem combinações recorrentes de áreas e tags que podem ajudar a sugerir categorias relacionadas durante o cadastro?

#### contempArt

Unidade de análise: um artista em início de carreira.

Perguntas:

1. Quantos artistas não possuem website informado e poderiam se beneficiar de um portfólio público próprio no FlowCarreiras?
2. Quantos possuem somente o Instagram informado como presença digital identificável?
3. Quantos possuem imagens registradas na base, mas não possuem website ou Instagram informado?
4. Quais sinais permitem separar presença digital de organização profissional, evitando tratar seguidores como sinônimo de carreira estruturada?

### 2. Visibilidade justa e dependência de plataformas

Dor relacionada: artistas dependem de algoritmos de engajamento, nos quais alcance não representa necessariamente qualidade, produção ou evolução profissional.

Perguntas para o contempArt:

1. A quantidade de publicações ou imagens registradas possui relação forte com seguidores e engajamento?
2. Existem artistas com volume de publicações ou imagens registradas acima da mediana, mas visibilidade abaixo da mediana?
3. Qual parcela dos seguidores e do engajamento está concentrada nos artistas de maior alcance?
4. Artistas com volume registrado semelhante apresentam níveis muito diferentes de visibilidade?
5. Quais perfis seriam pouco descobertos caso uma plataforma recomendasse artistas somente por seguidores ou curtidas?
6. Que indicadores podem apoiar uma fila de descoberta equilibrada sem criar um ranking de popularidade?

### 3. Mentoria e acesso a oportunidades

Dor relacionada: artistas não sabem qual o próximo passo da carreira e encontram oportunidades dispersas.

Perguntas para o Mapa Cultural de Pernambuco:

1. Quais áreas e tags reúnem mais artistas no recorte e podem orientar a cobertura inicial de mentores e oportunidades?
2. Quais áreas possuem presença menor no recorte e precisam de cuidado para não desaparecer dos filtros e recomendações?
3. Quais combinações de áreas, funções e tags podem apoiar compatibilidade entre necessidades, expertise, mentores e oportunidades?
4. Quais perfis possuem pouca informação profissional para receber recomendações confiáveis, reforçando a importância de completar o onboarding?

As bases externas não informam quem busca mentoria, quais oportunidades foram acessadas ou quais necessidades cada artista possui. Portanto, essas perguntas orientam a estrutura do produto, mas não avaliam o sucesso real do match.

## Perguntas futuras com dados próprios do FlowCarreiras

Estas perguntas avaliam o aplicativo em si e deverão ser respondidas quando houver dados reais de uso:

1. Em quais etapas os artistas abandonam ou pulam o onboarding?
2. Completar bio, área, tags, foto e links aumenta a criação e o compartilhamento do portfólio?
3. Quanto tempo um novo usuário leva para publicar sua primeira obra?
4. Quais áreas e tags possuem maior procura por mentoria e menor oferta de mentores?
5. Os matches com maior compatibilidade de tags resultam em mentorias iniciadas e concluídas?
6. Quais tipos de oportunidade recebem mais visualizações e acessos por área artística?
7. As notificações por área e tags aumentam o acesso a oportunidades relevantes?
8. A fila de descoberta distribui exposição entre artistas novos e antigos sem concentrar visualizações nos mesmos perfis?
9. Artistas com pouca presença em redes sociais conseguem receber descoberta, contatos ou oportunidades dentro da plataforma?
10. O uso contínuo do FlowCarreiras aumenta completude do perfil, quantidade de obras organizadas e conexões profissionais?

Parte desses indicadores ainda exige instrumentação de eventos no aplicativo, como registrar visualizações, compartilhamentos, acessos externos, aparições na fila de descoberta e abandono de etapas.

## Uso conjunto sem integração

Os resultados podem aparecer na mesma narrativa porque apoiam partes diferentes do FlowCarreiras. Cada gráfico, métrica e conclusão deve indicar claramente qual dataset foi usado.

Não haverá junção linha a linha, identificação de correspondências entre pessoas ou comparação direta entre Pernambuco e Alemanha.

## Regra de interpretação

Os dados externos identificam sinais e sustentam decisões de design, mas não comprovam sozinhos as dores declaradas pelos artistas nem a eficácia do FlowCarreiras. Afirmações sobre impacto do aplicativo dependerão dos dados próprios definidos neste plano.

A matriz completa de cobertura está em `docs/matriz_cobertura_analitica.md`, e os dados próprios necessários estão definidos em `docs/plano_instrumentacao.md`.

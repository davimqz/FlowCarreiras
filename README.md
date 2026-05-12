# Flow Carreiras

Plataforma PWA para artistas emergentes de Recife — portfólio digital, mentoria e oportunidades culturais.

## Stack

| Camada | Tecnologia |
|--------|-----------|
| Backend | Java 17 + Spring Boot 4.x + Spring Security 7 (JWT) |
| Banco (dev) | H2 in-memory |
| Banco (prod) | PostgreSQL |
| Frontend | React 18 + Vite + Tailwind CSS |
| PWA | vite-plugin-pwa + Workbox |
| Upload | Sistema de arquivos local (`./uploads/`) |

---

## Estrutura do projeto

```
FlowCarreiras/
├── flowcarreiras-api/     ← Spring Boot
│   └── src/main/java/com/flowcarreiras/flowcarreiras_api/
│       ├── model/         ← Entidades JPA
│       ├── repository/    ← Spring Data
│       ├── service/       ← Lógica de negócio
│       ├── controller/    ← REST endpoints
│       ├── dto/           ← Request/Response
│       ├── security/      ← JWT filter + UserDetails
│       ├── config/        ← Security, CORS, WebMvc, DataInitializer
│       └── exception/     ← Handler global
└── flowcarreiras-web/     ← React + Vite
    └── src/
        ├── api/           ← Chamadas HTTP (axios)
        ├── components/    ← Componentes reutilizáveis
        ├── context/       ← AuthContext
        ├── hooks/         ← useFileUpload, useTagAutocomplete
        └── pages/         ← Login, MinhasObras, NovaObra, EditarObra, PortfolioPublico
```

---

## Rodar o backend

```bash
cd flowcarreiras-api
./mvnw spring-boot:run          # Unix
mvnw.cmd spring-boot:run        # Windows
```

- API disponível em: `http://localhost:8080`
- H2 Console: `http://localhost:8080/h2-console`
  - JDBC URL: `jdbc:h2:mem:flowcarreiras`
  - User: `sa` / Senha: (vazio)

**Seed automático na inicialização:**
- 25 tags em 4 categorias
- Usuário de teste: `marina@test.com` / `senha123`

### Para usar PostgreSQL (produção)

```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=prod \
  -Dspring-boot.run.arguments="--DB_USERNAME=postgres --DB_PASSWORD=suasenha"
```

---

## Rodar o frontend

```bash
cd flowcarreiras-web
npm install
npm run dev        # Dev server em http://localhost:5173
npm run build      # Build de produção
npm run preview    # Preview do build
```

O Vite faz proxy automático de `/api` → `http://localhost:8080`.

---

## API REST — Endpoints principais

### Auth (público)
| Método | Endpoint | Descrição |
|--------|----------|-----------|
| POST | `/api/auth/registro` | Criar conta |
| POST | `/api/auth/login` | Login → retorna JWT |

### Obras (autenticado exceto GETs)
| Método | Endpoint | Auth | Descrição |
|--------|----------|------|-----------|
| GET | `/api/obras/{id}` | Não | Detalhe de uma obra |
| GET | `/api/obras/artista/{artistaId}` | Não | Obras de um artista |
| GET | `/api/obras/publico/{urlPublica}` | Não | Portfólio público completo |
| POST | `/api/obras` | Sim | Upload de nova obra |
| PUT | `/api/obras/{id}` | Sim | Editar obra |
| DELETE | `/api/obras/{id}` | Sim | Remover obra (permanente) |

### Tags (público)
| Método | Endpoint | Descrição |
|--------|----------|-----------|
| GET | `/api/tags` | Todas as tags |
| GET | `/api/tags/search?q=ilustra` | Autocomplete |

### Upload (multipart/form-data)

```
POST /api/obras
Authorization: Bearer <token>
Content-Type: multipart/form-data

dados: <JSON Blob, Content-Type: application/json>
  {
    "titulo": "Minha obra",
    "tipoMidia": "IMAGEM",
    "status": "PUBLICADA",
    "tagIds": ["uuid1", "uuid2"]
  }

file: <arquivo binário, opcional para EMBED>
```

---

## Funcionalidades implementadas

### H2.1 — Upload de obras
- Formatos: JPG/PNG (≤10 MB), MP3/WAV e MP4 (≤30 MB), YouTube/Vimeo embed
- Drag-and-drop com preview de imagem
- Barra de progresso em tempo real
- Validação de formato e tamanho no frontend e no backend
- Mensagem de erro com formulário preservado em falha de rede

### H2.2 — Categorização por tags
- Taxonomia fechada (seed com 25 tags)
- Autocomplete com debounce de 250ms
- Chips removíveis, máximo de 10 tags
- Bloqueio de publicação sem tags

### H2.3 — Edição e remoção
- Edição de todos os campos incluindo substituição de arquivo
- Modal de confirmação antes de remover
- Remoção permanente (banco + arquivo)
- Apenas o dono pode editar/remover (validado no backend)

### H2.4 — Visualização pública
- URL pública gerada no cadastro (`nome-artista-{hash}`)
- Acessível sem login
- Botão "Copiar link" com feedback visual
- Botão "Ver como público" em nova aba

### H2.5 — Redes instáveis
- PWA com Service Worker (Workbox)
- Estratégia Network-First para obras
- Cache-First para mídias (7 dias)
- Lazy loading em todas as imagens
- Skeleton screens durante carregamento
- Indicador discreto de status offline

---

## Variáveis de configuração (application.yml)

```yaml
storage:
  upload-dir: ./uploads       # Diretório de arquivos

jwt:
  secret: <base64-256bits>    # Chave JWT
  expiration: 86400000        # 24 horas em ms

spring.servlet.multipart:
  max-file-size: 30MB
  max-request-size: 35MB
```

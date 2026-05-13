# Docker — Flow Carreiras

Guia completo para rodar o projeto com Docker (sem instalar Java, PostgreSQL ou Node localmente).

---

## Pré-requisitos

- [Docker Desktop](https://www.docker.com/products/docker-desktop/) (inclui Docker Compose)
- Git

Verifique a instalação:
```bash
docker --version        # Docker 24+
docker compose version  # Docker Compose 2.x
```

---

## Estrutura dos arquivos Docker

```
FlowCarreiras/
├── docker-compose.yml          # Produção (PostgreSQL + Backend + Frontend/Nginx)
├── docker-compose.dev.yml      # Overlay de desenvolvimento (hot-reload)
├── .env.example                # Template de variáveis de ambiente
├── uploads/                    # Arquivos enviados (persistidos no host)
├── flowcarreiras-api/
│   ├── Dockerfile              # Multi-stage: Maven build → JRE runtime
│   └── .dockerignore
└── flowcarreiras-web/
    ├── Dockerfile              # Multi-stage: Vite build → Nginx
    ├── Dockerfile.dev          # Vite dev server com hot-reload
    ├── nginx.conf              # Config Nginx para SPA + proxy /api e /uploads
    └── .dockerignore
```

---

## Setup inicial (primeira vez)

```bash
# 1. Clone o projeto
git clone <url-do-repo>
cd FlowCarreiras

# 2. Crie o arquivo de variáveis de ambiente
cp .env.example .env
# Edite .env com suas senhas (especialmente POSTGRES_PASSWORD e JWT_SECRET em produção)

# 3. Garanta que a pasta uploads existe
mkdir -p uploads

# 4. Suba todos os serviços
docker compose up -d
```

Aguarde alguns minutos no primeiro start (Maven baixa dependências, Vite faz build).

Acesse: **http://localhost**

---

## Comandos — Produção

```bash
# Subir todos os serviços em background
docker compose up -d

# Ver status dos containers
docker compose ps

# Ver logs de todos os serviços
docker compose logs -f

# Ver logs de um serviço específico
docker compose logs -f backend
docker compose logs -f db
docker compose logs -f frontend

# Rebuild após mudanças no código
docker compose up -d --build

# Rebuild de um serviço específico
docker compose up -d --build backend

# Parar todos os containers (mantém volumes)
docker compose down

# Parar e remover volumes — CUIDADO: apaga todos os dados do banco
docker compose down -v
```

---

## Comandos — Desenvolvimento (hot-reload)

Em desenvolvimento, o frontend usa o Vite dev server (porta 5173) com hot-reload.
O backend continua rodando em container Docker conectado ao PostgreSQL.

```bash
# Subir em modo dev
docker compose -f docker-compose.yml -f docker-compose.dev.yml up

# Rebuild em dev
docker compose -f docker-compose.yml -f docker-compose.dev.yml up --build

# Parar
docker compose -f docker-compose.yml -f docker-compose.dev.yml down
```

Acesse:
- Frontend (hot-reload): **http://localhost:5173**
- API: **http://localhost:8080**
- PostgreSQL: `localhost:5432` (exposto em dev para uso com DBeaver/psql)

---

## Acessando os serviços

| Serviço     | Produção               | Desenvolvimento         |
|-------------|------------------------|-------------------------|
| Frontend    | http://localhost       | http://localhost:5173   |
| API         | http://localhost:8080  | http://localhost:8080   |
| PostgreSQL  | não exposto            | localhost:5432          |

---

## Utilitários

```bash
# Abrir shell no container do backend
docker compose exec backend sh

# Abrir psql no banco de dados
docker compose exec db psql -U postgres -d flowcarreiras

# Listar tabelas no banco
docker compose exec db psql -U postgres -d flowcarreiras -c '\dt'

# Ver espaço usado por volumes e imagens
docker system df -v

# Limpar imagens, containers e redes não utilizados (sem remover volumes)
docker system prune -a

# Limpar TUDO incluindo volumes — perde dados do banco
docker system prune -a --volumes
```

---

## Troubleshooting

### 1. "Cannot connect to database" / backend não inicia

```bash
# Verificar se o banco está healthy
docker compose ps

# Ver logs do banco
docker compose logs db

# O backend espera o banco estar healthy antes de iniciar.
# Se o banco demorar, o backend vai reiniciar automaticamente.
```

### 2. "Port 8080 already in use" ou "Port 80 already in use"

```bash
# Windows — ver quem está usando a porta
netstat -ano | findstr :8080

# Matar o processo (substitua PID pelo número encontrado)
taskkill /PID <PID> /F

# Ou altere a porta no docker-compose.yml:
# ports:
#   - "8081:8080"   # host:container
```

### 3. Frontend não conecta na API (erro de CORS ou rede)

- Em **produção**: o Nginx faz proxy de `/api` → `backend:8080`. Não é necessário configurar CORS para chamadas do frontend.
- Em **desenvolvimento**: o Vite dev server faz proxy de `/api` → `http://localhost:8080`. Verifique se o backend está rodando.
- Confirme que o React usa caminhos relativos (`/api/...`) e não URLs absolutas com `localhost:8080`.

```bash
# Verificar se o backend está up e respondendo
curl http://localhost:8080/api/tags
```

### 4. Uploads não aparecem ou somem após restart

```bash
# Verificar se a pasta uploads existe no host
ls uploads/

# Verificar se o volume está montado corretamente
docker compose config | grep -A5 uploads

# Verificar permissões (Linux/Mac)
ls -la uploads/
```

O backend salva arquivos em `/app/uploads` dentro do container, que é mapeado para `./uploads/` no host via bind mount. Os arquivos devem persistir entre restarts.

### 5. Build do frontend falha

```bash
# Rebuild sem cache
docker compose build --no-cache frontend

# Ver erro detalhado
docker compose build frontend 2>&1 | tail -50
```

### 6. Hot-reload não funciona em desenvolvimento

```bash
# Confirmar que está usando o docker-compose.dev.yml
docker compose -f docker-compose.yml -f docker-compose.dev.yml ps

# Reiniciar só o frontend
docker compose -f docker-compose.yml -f docker-compose.dev.yml restart frontend

# Ver logs do frontend
docker compose -f docker-compose.yml -f docker-compose.dev.yml logs -f frontend
```

### 7. Backend demora muito para iniciar (Spring Boot)

O start_period do health check é 90 segundos — o Spring Boot pode demorar para iniciar na primeira vez enquanto o Hibernate cria as tabelas. Aguarde e monitore:

```bash
docker compose logs -f backend
```

Quando aparecer `Started FlowcarreirasApiApplication`, o backend está pronto.

### 8. Recomeçar do zero

```bash
# Remove containers, volumes e imagens do projeto
docker compose down -v
docker compose build --no-cache
docker compose up -d
```

---

## Variáveis de ambiente (.env)

Copie `.env.example` para `.env` e ajuste antes de subir em produção:

| Variável           | Padrão                     | Descrição                              |
|--------------------|----------------------------|----------------------------------------|
| `POSTGRES_DB`      | `flowcarreiras`            | Nome do banco de dados                 |
| `POSTGRES_USER`    | `postgres`                 | Usuário do banco                       |
| `POSTGRES_PASSWORD`| `postgres`                 | **Troque em produção!**                |
| `JWT_SECRET`       | (valor de desenvolvimento) | **Gere um secret forte em produção!**  |
| `SEED_PASSWORD`    | `senha123`                 | Senha do usuário `marina@test.com`     |

Para gerar um JWT_SECRET seguro:
```bash
# Linux/Mac
openssl rand -base64 64

# Windows PowerShell
[Convert]::ToBase64String([Security.Cryptography.RandomNumberGenerator]::GetBytes(64))
```

---

## Perfis Spring Boot

| Perfil   | Banco          | DDL auto  | Quando usar                      |
|----------|----------------|-----------|----------------------------------|
| (padrão) | H2 in-memory   | create-drop | Desenvolvimento local sem Docker |
| `docker` | PostgreSQL (db) | update   | Docker Compose                   |
| `prod`   | PostgreSQL local| validate | Deploy manual sem Docker          |

O `docker-compose.yml` ativa automaticamente `SPRING_PROFILES_ACTIVE=docker`.

---

## Arquitetura Docker

```
┌─────────────────────────────────────────────────┐
│                  flowcarreiras-net               │
│                                                  │
│  ┌──────────┐    ┌──────────┐    ┌────────────┐ │
│  │    db    │◄───│ backend  │◄───│  frontend  │ │
│  │ :5432    │    │ :8080    │    │ Nginx :80  │ │
│  │ postgres │    │ Spring   │    │            │ │
│  └──────────┘    └──────────┘    └────────────┘ │
│       │               │                         │
└───────┼───────────────┼─────────────────────────┘
        │               │
   postgres_data    ./uploads/
   (volume)         (bind mount)
```

Fluxo de requisição (produção):

```
Browser → Nginx :80 → /api/* → backend:8080 → PostgreSQL
                     /uploads/* → backend:8080 → ./uploads/
                     /* → index.html (SPA)
```

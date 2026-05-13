# Flow Carreiras

## Como rodar

### Pré-requisito

[Docker Desktop](https://www.docker.com/products/docker-desktop/) instalado e rodando.

### Linux / Mac

```bash
git clone <url-do-repo>
cd FlowCarreiras
cp .env.example .env
mkdir -p uploads
docker compose up -d
```

### Windows (PowerShell)

```powershell
git clone <url-do-repo>
cd FlowCarreiras
cp .env.example .env
mkdir uploads -ErrorAction SilentlyContinue
docker compose up -d
```

Acesse em: **[http://localhost](http://localhost)**

Usuário de teste: `marina@test.com` / `senha123`

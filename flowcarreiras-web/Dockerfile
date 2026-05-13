# ============================================================
# Stage 1: Build — compila o bundle de produção com Vite
# ============================================================
FROM node:20-alpine AS builder

WORKDIR /app

COPY package.json package-lock.json ./
RUN npm ci

COPY . .

# VITE_API_URL é injetado em tempo de build para o bundle JS.
# Em produção Docker com Nginx como proxy, não é necessário
# (as chamadas vão para /api relativo), mas pode ser sobrescrito.
ARG VITE_API_URL=""
ENV VITE_API_URL=$VITE_API_URL

RUN npm run build

# ============================================================
# Stage 2: Runtime — Nginx serve os arquivos estáticos
# ============================================================
FROM nginx:1.27-alpine AS runtime

COPY --from=builder /app/dist /usr/share/nginx/html
COPY nginx.conf /etc/nginx/conf.d/default.conf

EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]

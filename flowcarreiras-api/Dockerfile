# ============================================================
# Stage 1: Build — compila o JAR com Maven Wrapper
# ============================================================
FROM eclipse-temurin:17-jdk-alpine AS builder

WORKDIR /app

# Copia o wrapper e o pom.xml antes do código-fonte para
# aproveitar o cache de layers do Docker nas dependências.
COPY .mvn/ .mvn/
COPY mvnw pom.xml ./
RUN chmod +x mvnw && ./mvnw dependency:go-offline -B

COPY src/ src/
RUN ./mvnw package -DskipTests -B

# ============================================================
# Stage 2: Runtime — imagem final enxuta apenas com o JAR
# ============================================================
FROM eclipse-temurin:17-jre-alpine AS runtime

# Usuário não-root para segurança
RUN addgroup -S spring && adduser -S spring -G spring

WORKDIR /app

# Diretório de uploads persistido via volume
RUN mkdir -p /app/uploads && chown -R spring:spring /app

COPY --from=builder --chown=spring:spring /app/target/*.jar app.jar

USER spring

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]

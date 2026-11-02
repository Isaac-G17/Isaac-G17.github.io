# Imagen del backend para producción (Render).

# Etapa 1: compilar el .jar con el JDK completo.
FROM eclipse-temurin:21.0.12_8-jdk-noble AS build
WORKDIR /app

# Primero solo el wrapper y el pom: así Docker reutiliza las dependencias
# descargadas mientras pom.xml no cambie.
COPY mvnw pom.xml ./
COPY .mvn .mvn
RUN chmod +x mvnw && ./mvnw --batch-mode dependency:go-offline

COPY src src
RUN ./mvnw --batch-mode package -DskipTests

# Etapa 2: ejecutar con solo el JRE, más liviano.
FROM eclipse-temurin:21.0.12_8-jre-noble
WORKDIR /app

# No correr la app como root.
RUN useradd --system --no-create-home app
USER app

COPY --from=build /app/target/portfolio-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080
# Por defecto la JVM usa solo el 25 % de la RAM del contenedor para el heap.
ENTRYPOINT ["java", "-XX:MaxRAMPercentage=75", "-jar", "app.jar"]

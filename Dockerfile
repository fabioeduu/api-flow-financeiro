FROM eclipse-temurin:24-jdk-alpine

# Define a pasta de trabalho dentro do container
WORKDIR /app

# Copia o arquivo .jar gerado pelo seu projeto para dentro do container
COPY target/*.jar app.jar

# Expõe a porta que o Spring Boot usa
EXPOSE 8080

# Comando para rodar a aplicação
ENTRYPOINT ["java", "-jar", "app.jar"]
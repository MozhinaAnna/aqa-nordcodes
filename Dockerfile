# Используем официальный образ Maven с нужной версией JDK (Java и Maven "из коробки")

FROM maven:3.9.6-eclipse-temurin-21 AS builder

# Устанавливаем рабочую директорию внутри контейнера
WORKDIR /app

# Копируем сначала pom.xml для кэширования зависимостей.
# Docker будет перекачивать их только если pom.xml изменился.
COPY pom.xml .
RUN mvn dependency:go-offline

# Теперь копируем весь исходный код
COPY src ./src

# Запускаем сборку проекта и тесты.
CMD ["mvn", "test", "-DsuiteXmlFile=smoke.xml"]
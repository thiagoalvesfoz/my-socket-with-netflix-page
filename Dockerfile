FROM adoptopenjdk/openjdk11:alpine-slim AS builder

WORKDIR /app
COPY ./src ./src
COPY ./resources ./resources

# COMPILE
RUN javac -d ./out/production ./src/com/sockets/*.java


# REDUCE
FROM adoptopenjdk/openjdk11:x86_64-alpine-jre-11.0.11_9

WORKDIR /app
COPY --from=builder /app/out/ /app/out/
COPY --from=builder /app/resources/ /app/resources

EXPOSE 8000
CMD ["java", "-Dfile.encoding=UTF-8", "-classpath", "./out/production", "com.sockets.Main"]

# Compilar
# javac -d ./out/production ./src/com/sockets/*.java
# Executar
# java -Dfile.encoding=UTF-8 -classpath $(pwd)/out/production com.sockets.Main
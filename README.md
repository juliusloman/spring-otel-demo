# Spring OpenTelemetry demo

Simple minimal springboot playground. Makes a http call to postman echo service for delay and calculates fibonacci number

## Run standalone (no instrumentation)
```
mvn spring-boot:run
```

## Post call
```
curl -XPOST http://localhost:8080/quote/postQuote -H "Content-Type: application/json" -d { "input": 10 }'
curl -XPOST http://localhost:8080/quote/postQuoteExtended -H "Content-Type: application/json" -d { "input": 10 }'
```

## Run with OpenTelemetry instrumentation
```
java -javaagent:opentelemetry-javaagent.jar -jar target/spring-otel-demo-1.0-SNAPSHOT.jar
```

## Run with Dynatrace OneAgent (PaaS)
```
/opt/dynatrace/oneagent/dynatrace-agent64.sh java -jar target/spring-otel-demo-1.0-SNAPSHOT.jar
```
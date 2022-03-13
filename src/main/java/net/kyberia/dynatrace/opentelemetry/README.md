# Spring OpenTelemetry demo

Simple minimal springboot playground.

## Run standalone (no instrumentation)
```
mvn spring-boot:run
```

## Create transaction
```
curl -XPOST http://localhost:8080/transactions/transaction -H "Content-Type: application/json" -d '{"id": "someid", "name": "somename"}'
```

## Run with OpenTelemetry instrumentation
```
mvn package
java -javaagent:opentelemetry-javaagent.jar -jar target/spring-otel-demo-1.0-SNAPSHOT.jar
```

## Run with Dynatrace OneAgent (PaaS)
```
/opt/dynatrace/oneagent/dynatrace-agent64.sh java -jar target/spring-otel-demo-1.0-SNAPSHOT.jar
```
#!/bin/bash

java -javaagent:../opentelemetry-javaagent.jar -Dotel.traces.exporter=jaeger -Dotel.exporter.jaeger.endpoint=http://localhost:14250 -Dotel.service.name=slsp-demo-service -jar target/spring-otel-demo-1.0-SNAPSHOT.jar
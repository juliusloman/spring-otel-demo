#!/bin/sh

../oneagent-slsp/dynatrace-agent64.sh java -javaagent:../opentelemetry-javaagent.jar -jar target/spring-otel-demo-1.0-SNAPSHOT.jar

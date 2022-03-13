package net.kyberia.dynatrace.opentelemetry.controller;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.opentelemetry.api.GlobalOpenTelemetry;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.SpanKind;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.context.Context;
import io.opentelemetry.context.Scope;
import io.opentelemetry.context.propagation.TextMapSetter;
import net.kyberia.dynatrace.opentelemetry.model.Transaction;

@RestController
@RequestMapping("/transactions")
public class TransactionController {
    Tracer tracer = GlobalOpenTelemetry.getTracer("kyberia-spring-otel-demo", "1.0.0");
    

    static final TextMapSetter<Map<String, String>> setter = new TextMapSetter<Map<String, String>>() {
        @Override
        public void set(Map<String, String> carrier, String key, String value) {
            carrier.put(key, value);
        }
    };

    @GetMapping("/transaction")
    public ResponseEntity<List<Transaction>> getTransactions() {
        return new ResponseEntity<>(new ArrayList<Transaction>(), HttpStatus.OK);
    }

    @PostMapping("/transaction")
    public ResponseEntity<String> createTransaction(@RequestBody Transaction transaction) {
        Span span = tracer.spanBuilder("Calling external service").setSpanKind(SpanKind.CLIENT).startSpan();

        Map<String, String> carrier = new HashMap<String, String>();
        
        try (Scope scope = span.makeCurrent()) {
            // Some random sleep
            Thread.sleep((long)(Math.random() * 1000));

            Span outGoing = tracer.spanBuilder("/3rdpartyresource").setSpanKind(SpanKind.CLIENT).startSpan();
            try (Scope scope2 = outGoing.makeCurrent()) {
                // Inject context
                GlobalOpenTelemetry.getPropagators().getTextMapPropagator().inject(Context.current(), carrier, setter);
            } finally {
              outGoing.end();
            }    

        } catch (InterruptedException e) {
        } finally {
            span.end();
        }
        return new ResponseEntity<>(String.format("Transaction was created, propagator %s carrier values %s",GlobalOpenTelemetry.getPropagators().getTextMapPropagator().getClass().getCanonicalName(), carrier.toString()), HttpStatus.CREATED);
    }
}

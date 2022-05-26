package sk.slsp.opentelemetry.demo.controller;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.opentelemetry.extension.annotations.SpanAttribute;
import io.opentelemetry.extension.annotations.WithSpan;
import io.opentelemetry.api.GlobalOpenTelemetry;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.StatusCode;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.context.Scope;
import sk.slsp.opentelemetry.demo.model.Quote;

@RestController
@RequestMapping("/quote")
public class QuoteController {
    static Logger logger = LoggerFactory.getLogger(QuoteController.class);
    Tracer tracer = GlobalOpenTelemetry.getTracer("slsp-otel-demo", "1.0.0");

    public static long fib(long n) {
        if ((n == 0) || (n == 1))
            return n;
        else
            return fib(n - 1) + fib(n - 2);
    }

    // @WithSpan
    // private void fetchQuoteData(@SpanAttribute("quote.input") long quoteInput) {
    @WithSpan
    private void fetchQuoteData(@SpanAttribute("quote.input") long quoteInput) {
        try {
            URL theUrl = new URL(String.format("https://postman-echo.com/delay/%d", quoteInput % 3));
            HttpURLConnection urlConnection = (HttpURLConnection) theUrl.openConnection();
            int responseCode = urlConnection.getResponseCode();
            logger.info("Response code: {}", responseCode);
        } catch (IOException ex) {
            logger.error(ex.getMessage(), ex);
        }
    }

    @PostMapping("/handleQuote")
    public ResponseEntity<Quote> handleQuote(@RequestBody Quote quote) {
        fetchQuoteData(quote.getInput());

        if (quote.getInput() < 0) {
        } else {
            quote.setValue(fib(quote.getInput()));
        }
        return ResponseEntity.ok(quote);
    }

    @PostMapping("/handleQuoteExtended")
    public ResponseEntity<Quote> handleQuoteExteded(@RequestBody Quote quote) {
        fetchQuoteData(quote.getInput());

        Span span = tracer.spanBuilder("calculate")
                .setAttribute("quote.input", quote.getInput())
                .startSpan();
        try (Scope scope = span.makeCurrent()) {
            span.addEvent("Init");
            if (quote.getInput() < 0) {
                span.setStatus(StatusCode.ERROR, "Quote must be non-negative!");
            } else {
                quote.setValue(fib(quote.getInput()));
            }
            span.addEvent("Finish");
        } finally {
            span.end();
        }
        return ResponseEntity.ok(quote);
    }
}

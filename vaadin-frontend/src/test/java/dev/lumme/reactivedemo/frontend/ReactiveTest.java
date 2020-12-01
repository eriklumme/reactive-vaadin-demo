package dev.lumme.reactivedemo.frontend;


import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;

public class ReactiveTest {

    @BeforeAll
    static void beforeAll() {
        Logger root = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
        root.setLevel(Level.INFO);
    }

    @Test
    void testAsynchronousStream() {
        System.out.println("Creating and subscribing to mono");

        Mono.delay(Duration.ZERO)
                .map(v -> "Hello World")
                .subscribe(System.out::println);

        System.out.println("Subscribed to mono");
    }

    @Test
    void testSynchronousStream() {
        System.out.println("Creating and subscribing to mono");

        Mono.just(0)
                .map(v -> "Hello World")
                .subscribe(System.out::println);

        System.out.println("Subscribed to mono");
    }

    @Test
    void testAsynchronousStream2() {
        System.out.println("Creating mono");

        Mono<String> mono = Mono.just(0)
                .map(v -> {
                    System.out.println("Mono is being evaluated");
                    return "Hello World";
                });

        System.out.println("Subscribing to mono");

        mono.subscribe(System.out::println);

        System.out.println("Subscribed to mono");
    }

    @Test
    void testWebClient() {
        WebClient webClient = WebClient.builder().build();

        Mono<String> responseBody = webClient
                .get()
                .uri("https://www.google.com")
                .retrieve()
                .bodyToMono(String.class);

        System.out.println(responseBody.block());

    }
}

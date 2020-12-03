package dev.lumme.reactivedemo.frontend.client;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

import dev.lumme.reactivedemo.common.client.CityClient;
import dev.lumme.reactivedemo.common.dto.CityDTO;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Service
public class CityClientImpl implements CityClient {

    private static final ExecutorService executorService = Executors.newFixedThreadPool(5);

    private final WebClient webClient;

    public CityClientImpl(WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public Mono<List<CityDTO>> findCitiesReactive() {
        return webClient.get().uri(CityClient.CITIES)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<CityDTO>>() {})
                .publishOn(Schedulers.fromExecutor(executorService));
    }

    @Override
    public void findCitiesAsync(Consumer<List<CityDTO>> consumer) {
        executorService.execute(
                () -> consumer.accept(this.findCitiesSync()));
    }

    @Override
    public List<CityDTO> findCitiesSync() {
        return webClient.get().uri(CityClient.CITIES)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<CityDTO>>() {})
                .block();
    }
}

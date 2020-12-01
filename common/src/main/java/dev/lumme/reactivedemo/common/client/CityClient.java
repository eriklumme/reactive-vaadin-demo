package dev.lumme.reactivedemo.common.client;

import dev.lumme.reactivedemo.common.dto.CityDTO;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.function.Consumer;

public interface CityClient {

    String CITIES = "/cities";

    Mono<List<CityDTO>> findCitiesReactive();

    List<CityDTO> findCitiesSync();

    void findCitiesAsync(Consumer<List<CityDTO>> consumer);
}

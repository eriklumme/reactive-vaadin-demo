package dev.lumme.reactivedemo.frontend.client;

import dev.lumme.reactivedemo.common.client.FileClient;
import dev.lumme.reactivedemo.common.dto.FileDTO;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
public class FileClientImpl implements FileClient {

    private final WebClient webClient = WebClient.builder().baseUrl("http://localhost:8082").build();

    @Override
    public List<FileDTO> getFiles(int offset, int limit) {
        return webClient.get().uri(uriBuilder -> uriBuilder
                .path(GetFiles.PATH)
                .queryParam(GetFiles.OFFSET, offset)
                .queryParam(GetFiles.LIMIT, limit)
                .build()
        ).retrieve().bodyToMono(new ParameterizedTypeReference<List<FileDTO>>() {}).block();

    }

    @Override
    public Integer countFiles() {
        return webClient.get().uri(CountFiles.PATH).retrieve().bodyToMono(Integer.class).block();
    }
}

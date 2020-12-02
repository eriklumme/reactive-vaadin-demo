package dev.lumme.reactivedemo.frontend.client;

import dev.lumme.reactivedemo.common.client.ChangePasswordClient;
import dev.lumme.reactivedemo.common.dto.ChangePasswordDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class ChangePasswordClientImpl implements ChangePasswordClient {

    private final WebClient webClient;

    public ChangePasswordClientImpl(WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public Mono<Integer> changePassword(String password) {
        return Mono.subscriberContext()
                .flatMap(context -> createChangePasswordRequest(context.getOrDefault("user", null), password));
    }

    private Mono<Integer> createChangePasswordRequest(String user, String password) {
        ChangePasswordDTO changePasswordDTO = new ChangePasswordDTO(user, password);

        return webClient.post().uri(CHANGE_PASSWORD)
                .bodyValue(changePasswordDTO)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, response -> Mono.empty())
                .toBodilessEntity()
                .map(ResponseEntity::getStatusCodeValue);
    }
}

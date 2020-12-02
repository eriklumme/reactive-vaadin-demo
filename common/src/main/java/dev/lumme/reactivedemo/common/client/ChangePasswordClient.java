package dev.lumme.reactivedemo.common.client;

import reactor.core.publisher.Mono;

public interface ChangePasswordClient {

    String CHANGE_PASSWORD = "/change-password";

    Mono<Integer> changePassword(String password);
}

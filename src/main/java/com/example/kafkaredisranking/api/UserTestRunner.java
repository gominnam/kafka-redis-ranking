package com.example.kafkaredisranking.api;

import com.example.kafkaredisranking.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.util.List;

@Component
public class UserTestRunner implements CommandLineRunner {

    private final WebClient webClient;

    private final List<String> users = List.of( // example 30 users
            "Alice", "Bob", "Charlie", "David", "Eve",
            "Jack", "Jill", "Kate", "Kim", "Leo",
            "Mike", "Nick", "Oscar", "Peter", "Queen",
            "Robert", "Sam", "Tom", "Victor", "Zoe",
            "Alex", "Ben", "Chris", "Dylan", "Frank",
            "Grace", "Helen", "Ivy", "Jake", "Liam"
    );

    private final int USER_COUNT = users.size();

    @Autowired
    public UserTestRunner(WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public void run(String... args) {
        Flux.fromIterable(users)
                .flatMap(this::createUser)
                .thenMany(Flux.interval(Duration.ofSeconds(5)))
                .flatMap(i -> addUserScore())
                .subscribeOn(Schedulers.boundedElastic())
                .subscribe();
    }

    private Mono<Void> createUser(String name) {
        UserDTO userDTO = new UserDTO();
        userDTO.setUserId(name);
        userDTO.setUserName(name);

        return webClient.post()
                .uri("/api/user/register")
                .bodyValue(userDTO)
                .retrieve()
                .bodyToMono(Void.class);
    }

    private Mono<Void> addUserScore() {
        int randomUserIndex = (int)(Math.random() * users.size());
        int randomScore = (int)(Math.random() * 10) + 1;

        UserDTO userDTO = new UserDTO();
        userDTO.setUserId(users.get(randomUserIndex));
        userDTO.setScore(randomScore);

        return webClient.post()
                .uri("/api/user/score")
                .bodyValue(userDTO)
                .retrieve()
                .bodyToMono(Void.class);
    }
}

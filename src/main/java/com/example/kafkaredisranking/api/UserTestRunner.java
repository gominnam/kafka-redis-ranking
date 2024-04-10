package com.example.kafkaredisranking.api;

import com.example.kafkaredisranking.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

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
        for(int i=0; i<USER_COUNT; i++) {
            createUser(users.get(i));
        }

        while(true) {
            addUserScore();
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // restore interrupted status
                break;
            }
        }
    }

    private void createUser(String name) {
        UserDTO userDTO = new UserDTO();
        userDTO.setUserId(name);
        userDTO.setUserName(name);

        WebClient.create()
                .post()
                .uri("/api/user/register")
                .bodyValue(userDTO)
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }

    private void addUserScore() {
        int randomUserIndex = (int)(Math.random() * USER_COUNT);
        int randomScore = (int)(Math.random() * 10) + 1;

        UserDTO userDTO = new UserDTO();
        userDTO.setUserId(users.get(randomUserIndex));
        userDTO.setScore(randomScore);

        WebClient.create()
                .post()
                .uri("/api/user/score")
                .bodyValue(userDTO)
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }
}

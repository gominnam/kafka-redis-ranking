package com.example.kafkaredisranking.controller;

import com.example.kafkaredisranking.dto.UserDTO;
import com.example.kafkaredisranking.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserScoreController {

    private final UserService userService;

    @Autowired
    public UserScoreController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/api/user/register")
    public ResponseEntity<Void> saveUser(@RequestBody UserDTO userDTO) {
        userService.saveUser(userDTO);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/api/user/score")
    public ResponseEntity<Void> addUserScore(@RequestBody UserDTO userDTO) {
        userService.addScoreByUserId(userDTO);
        return ResponseEntity.ok().build();
    }
}

package controllers;

import domain.UserService;
import models.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class UserController {
    private final UserService userService;

    public UserController(UserService service){
        this.userService = service;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String, String> credentials){
        String username = credentials.get("username");
        String email = credentials.get("email");
        String rawPassword = credentials.get("password");

        // 1. Hash the password
        String hashedPassword = passwordEncoder.encode(rawPassword);
        User user = new User(username, hashedPassword, email);

        userService.add(user);
    }
}

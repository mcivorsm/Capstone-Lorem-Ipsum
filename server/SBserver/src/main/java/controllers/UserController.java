package controllers;

import domain.Result;
import domain.UserService;
import models.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/user")
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



        User user = new User(username, rawPassword, email);

        Result<User> result = userService.add(user);

        // Check if user was successfully added
        if (result.isSuccess()) {
            // Return a success response with the created user
            return new ResponseEntity<>(result.getPayload(), HttpStatus.CREATED);
        }
        return ErrorResponse.build(result);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> delete(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();

        boolean deleted = userService.deleteById(user.getId(),user.getProfile().getProfileId());

        if (!deleted) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        }

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);


    }

    @PutMapping("/edit")
    public ResponseEntity<?>update(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();

        Result<User> result = userService.update(user);

        if(result.isSuccess()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return ErrorResponse.build(result);
    }
}

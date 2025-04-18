package learn.ligr.controllers;


import learn.ligr.controllers.ErrorResponse;
import learn.ligr.domain.Result;
import learn.ligr.domain.UserService;
import learn.ligr.models.Game;
import learn.ligr.models.Profile;
import learn.ligr.models.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    public UserController(UserService service){
        this.userService = service;
    }

    @GetMapping()
    public ResponseEntity<?> findAll() { // handles get route find by username
        List<User> users = userService.findAll();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/id/{userId}")
    public User findById(@PathVariable int userId) {
        return userService.findById(userId);
    }

        @GetMapping("/username/{username}")
    public ResponseEntity<?> findById(@PathVariable String username) { // handles get route find by username
        User user = userService.findByUsername(username);
        return new ResponseEntity<>(user, HttpStatus.OK);
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
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete")
    public ResponseEntity<?> delete(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();

        boolean deleted = userService.deleteById(user.getId(),user.getProfile().getProfileId()).isSuccess();

        if (!deleted) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        }

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);


    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<?> delete(@PathVariable int userId){
        User user = userService.findById(userId);

        Result<User> result = userService.deleteById(userId,user.getProfile().getProfileId());

        boolean deleted = result.isSuccess();

        if (!deleted) {
            result.getMessages().forEach(System.out::println);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/edit")
    public ResponseEntity<?>update(@RequestBody @Valid User user, BindingResult bindingResult){
        // automatic validation
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(bindingResult.getAllErrors(), HttpStatus.BAD_REQUEST);
        }

        Result<User> result = userService.update(user);

        if(result.isSuccess()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return ErrorResponse.build(result);
    }
}

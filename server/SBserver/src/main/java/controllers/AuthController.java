package controllers;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.RestController;
import security.JwtConverter;

@RestController
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final JwtConverter converter;

    public AuthController(AuthenticationManager authenticationManager, JwtConverter converter) {
        this.authenticationManager = authenticationManager;
        this.converter = converter;
}

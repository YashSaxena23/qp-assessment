package qp.grocery.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;
import qp.grocery.config.AuthenticationService;
import qp.grocery.config.JwtTokenProvider;
import qp.grocery.model.LoginRequest;

import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/grocery/auth")
public class LoginController {

    @Autowired
    AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody LoginRequest loginRequest) {
        System.out.println(loginRequest.getUsername());
        Map<String, String> tokens = authenticationService.authenticateUser(loginRequest.getUsername(),
                loginRequest.getPassword());

        return new ResponseEntity<>(tokens, HttpStatus.OK);
    }
}

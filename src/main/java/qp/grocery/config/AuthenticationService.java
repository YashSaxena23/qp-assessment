package qp.grocery.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AuthenticationService {


    /*
    In modern versions of Spring, the @Autowired annotation is often optional for constructor injection.
    When a class has only one constructor, Spring automatically considers it as the
    constructor to be used for dependency injection.
    In such cases, you don't necessarily need to explicitly use @Autowired on the constructor.
     */
//    private final AuthenticationManager authenticationManager;
//    private final UserDetailsService userDetailsService;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @Autowired
    MyUserDetailsService myUserDetailsService;

//    public AuthenticationService(JwtTokenProvider jwtTokenProvider) {
////        this.authenticationManager = authenticationManager;
////        this.userDetailsService = userDetailsService;
//        this.jwtTokenProvider = jwtTokenProvider;
//    }

    public Map<String, String> authenticateUser(String username, String password) {
        // Authentication logic
//        Authentication authentication = authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(username, password)
//        );

        // Generate access and refresh tokens with dynamically determined role
        UserDetails userDetails = myUserDetailsService.loadUserByUsername(username);
        String accessToken = jwtTokenProvider.generateToken(userDetails.getUsername(),
                userDetails.getAuthorities().iterator().next().getAuthority());
        String refreshToken = jwtTokenProvider.generateRefreshToken(userDetails.getUsername(),
                new HashMap<>());

        Map<String, String> tokens = new HashMap<>();
        tokens.put("access_token", accessToken);
        tokens.put("refresh_token", refreshToken);

        return tokens;
    }
}

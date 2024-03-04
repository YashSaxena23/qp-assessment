package qp.grocery.config;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import qp.grocery.model.LoginRequest;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.*;


@Component
public class JwtTokenProvider {

    @Value("${JWT_SECRET}")
    private String jwtSecret;

    @Value("${jwt.expiration}")
    private int jwtExpirationInMs;

    @Value("${jwt.refresh.expiration}")
    private int jwtRefreshTokenExpirationInMs;

    @PostConstruct
    protected void init() {
        jwtSecret = Base64.getEncoder().encodeToString(jwtSecret.getBytes());
    }

    public String generateToken(String username, String role) {
        Date date = new Date();
        Date expiryDate = new Date(date.getTime() + jwtExpirationInMs);

        return Jwts.builder()
                .setSubject(username)
                .claim("role", role)
                .setIssuedAt(date)
                .setExpiration(expiryDate)
                .signWith(Keys.hmacShaKeyFor(jwtSecret.getBytes()), SignatureAlgorithm.HS512)
                .compact();
    }

    public String generateRefreshToken(String username, Map<String, Object> claims) {
        Date date = new Date();
        Date expiryDate = new Date(date.getTime() + jwtRefreshTokenExpirationInMs);

        return Jwts.builder()
                .setSubject(username)
                .addClaims(claims)
                .setIssuedAt(date)
                .setExpiration(expiryDate)
                .signWith(Keys.hmacShaKeyFor(jwtSecret.getBytes()), SignatureAlgorithm.HS512)
                .compact();
    }



    public Long getId(String token) {
        JwtParser jwtParser = Jwts.parserBuilder()
                .setSigningKey(jwtSecret)
                .build();

        Jws<Claims> claimsJws = jwtParser.parseClaimsJws(token);
        Claims body = claimsJws.getBody();

        return Long.parseLong(body.get("id")+"");
    }

    public String resolveToken(HttpServletRequest req) {
        String bearerToken = req.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    public String getUserName(String token) {
        JwtParser jwtParser = Jwts.parserBuilder()
                .setSigningKey(jwtSecret)
                .build();

        Jws<Claims> claimsJws = jwtParser.parseClaimsJws(token);
        Claims body = claimsJws.getBody();
        return body.getSubject();
    }

    public Authentication getAuthentication(String token) {
        Role roleType = Role.valueOf(getRoleType(token));
        String username = getUserName(token);


        UserDetails userDetails = new User(username,
                null, new ArrayList<>(Collections.singletonList(roleType)));
        return new UsernamePasswordAuthenticationToken(userDetails, getId(token),
                userDetails.getAuthorities());
    }

    public boolean validateToken(String token, HttpServletRequest req) throws Exception {
        try {
            if (!resolveId(token, req))
                throw new Exception("Id Mismatch");
            Jwts.parserBuilder().setSigningKey(jwtSecret).build().parseClaimsJws(token);
            req.setAttribute("X_ROLE_",getRoleType(token));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Expired or invalid JWT token");
        }
    }


    public String getRoleType(String token) {
        JwtParser jwtParser = Jwts.parserBuilder()
                .setSigningKey(jwtSecret)
                .build();

        Jws<Claims> claimsJws = jwtParser.parseClaimsJws(token);
        Claims body = claimsJws.getBody();
        return body.get("role").toString();
    }

    public boolean resolveId(String token, HttpServletRequest req) {
        String id = req.getHeader("id");
        String tokenId = getId(token) + "";
        if (id != null && !id.isEmpty() && id.equals(tokenId)) {
            return true;
        }
        return false;
    }
}

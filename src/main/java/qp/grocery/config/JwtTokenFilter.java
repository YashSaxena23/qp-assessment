package qp.grocery.config;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.security.core.Authentication;


/**
 * For each incoming HTTP request, it intercepts the request before it reaches the controller methods.
 * The filter checks for the presence of a valid JWT token in the Authorization header.
 * If a valid token is found, it sets up the Spring Security context with the authenticated user.
 * ------------------------------------------------------------------------------------------------
 * SecurityConfig sets up the overall security configuration,
 * and JwtTokenFilter ensures that incoming requests are validated against the provided JWT tokens.
 */
public class JwtTokenFilter extends GenericFilterBean {
    private final JwtTokenProvider jwtTokenProvider;

    public JwtTokenFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }


    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String token = jwtTokenProvider.resolveToken((HttpServletRequest) servletRequest);
        try {
            if (token != null && jwtTokenProvider.validateToken(token, (HttpServletRequest) servletRequest)) {
                Authentication auth = jwtTokenProvider.getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }catch (Exception ex) {
            HttpServletResponse response = (HttpServletResponse) servletResponse;
            response.sendError(401, ex.getMessage());
            return;
        }

        filterChain.doFilter(servletRequest, servletResponse);


    }
}

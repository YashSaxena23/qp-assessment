package qp.grocery.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


/**
 * About ->
 * During the startup of Spring Boot application, the SecurityConfig class is loaded,
 * and Spring Security is configured based on the rules defined in this class.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtTokenProvider jwtTokenProvider;

    public SecurityConfig(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }


    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.authorizeRequests()
                .antMatchers("/grocery/auth").permitAll()
                .antMatchers("/h2-console/**").permitAll()
                .antMatchers("/grocery/user/**").hasRole("USER")
                .antMatchers("/grocery/admin/**").hasRole("ADMIN")
                .and()
                .addFilterBefore(new JwtTokenFilter(jwtTokenProvider),
                        UsernamePasswordAuthenticationFilter.class);

        http.headers().frameOptions().disable();
    }
}

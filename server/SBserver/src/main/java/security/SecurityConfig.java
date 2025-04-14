package security;

import domain.UserValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtConverter converter;

    @Autowired
    private UserValidationService userValidationService;

    public SecurityConfig(JwtConverter converter) {
        this.converter = converter;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.csrf().disable(); // 1

        http.authorizeRequests() // 2
                .antMatchers("/authenticate").permitAll()
                .antMatchers("/refresh_token").authenticated()
                .antMatchers(HttpMethod.GET, "/sighting", "/sighting/*").permitAll()
                .antMatchers(HttpMethod.POST, "/sighting").hasAnyRole("USER", "ADMIN")
                .antMatchers(HttpMethod.PUT, "/sighting/*").hasAnyRole("USER", "ADMIN")
                .antMatchers(HttpMethod.DELETE, "/sighting/*").hasRole("ADMIN")
                .and()
                .addFilter(new JwtRequestFilter(authenticationManager(), converter)) // 3
                .sessionManagement() // 4
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userValidationService)  // Set UserDetailsService
                .passwordEncoder(new BCryptPasswordEncoder());  // Set PasswordEncoder
    }


}

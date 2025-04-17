package learn.ligr.security;

import learn.ligr.domain.UserValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

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

        http.cors().and()
                .csrf().disable(); // 1

        http.authorizeRequests() // 2
                // anyone
                .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                .antMatchers("/login/authenticate").permitAll()
                .antMatchers(HttpMethod.POST, "/user/register").permitAll()
                .antMatchers("/refresh_token").authenticated()
                .antMatchers(HttpMethod.GET, "/**").permitAll()

                // authenticated users only
                .antMatchers(HttpMethod.POST, "/gameReview").hasAnyRole("USER", "ADMIN")
                .antMatchers(HttpMethod.PUT, "/gameReview/*", "/profile/edit").hasAnyRole("USER", "ADMIN")
                .antMatchers(HttpMethod.DELETE,  "/gameReview/*", "/user/delete").hasAnyRole("USER", "ADMIN")
                .antMatchers(HttpMethod.PUT,  "/user/edit").hasAnyRole("USER", "ADMIN")

                // admin only
                .antMatchers(HttpMethod.POST, "/game").hasRole("ADMIN")
                .antMatchers(HttpMethod.PUT, "/game/*").hasRole("ADMIN")
                .antMatchers(HttpMethod.DELETE, "/game/*", "/user/delete/*").hasRole("ADMIN")
                .anyRequest().authenticated()
                .and()
                .addFilterBefore(new JwtRequestFilter(authenticationManager(), converter), JwtRequestFilter.class) // 3
                .sessionManagement() // 4
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager(); // Automatically wires with your UserDetailsService
    }
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userValidationService)  // Set UserDetailsService
                .passwordEncoder(passwordEncoder());  // Set PasswordEncoder
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://localhost:3000"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }



}

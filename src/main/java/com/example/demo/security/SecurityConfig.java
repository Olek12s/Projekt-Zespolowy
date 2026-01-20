package com.example.demo.security;


import jakarta.servlet.Filter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig
{
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtAuthFilter jwtAuthFilter, AuthenticationProvider authenticationProvider) throws Exception
    {
        http.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        //.requestMatchers("/api/auth/**").permitAll()
                        //.authorizeHttpRequests()
                        //.requestMatchers("/api/admin/**").hasRole("ADMIN")
                        //.requestMatchers("/api/users/self").hasAnyRole("USER", "ADMIN")
                        //.requestMatchers("/api/users/**").hasRole("ADMIN")
                        // PUBLIC
                        .requestMatchers("/api/users/register", "/api/auth/login").permitAll()
                        .requestMatchers("/api/payments/webhook").permitAll()
                        .requestMatchers("/api/payments/success", "/api/payments/cancel").permitAll()

                        // ADMIN
                        .requestMatchers("/api/admin/**").hasRole("ADMIN") // W JWT: ROLE_ADMIN

                        // USER + ADMIN
                        .requestMatchers("/api/users/self").hasAnyRole("USER", "ADMIN")

                        // USER
                        .requestMatchers(HttpMethod.GET, "/api/vehicles/available").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/users/**").hasAnyRole("USER", "ADMIN")

                        // RESZTA
                        .anyRequest().authenticated()
                )
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore((Filter) jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);   // dziwne rzutowanie

        return http.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder)
    {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }

    @Bean
    public PasswordEncoder passwordEncoder()
    {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception
    {
        return config.getAuthenticationManager();
    }
}
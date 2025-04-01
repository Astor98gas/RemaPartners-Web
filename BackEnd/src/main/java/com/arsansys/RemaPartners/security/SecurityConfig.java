package com.arsansys.RemaPartners.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.arsansys.RemaPartners.security.filters.JwtAuthenticationFilter;
import com.arsansys.RemaPartners.security.filters.JwtAutorizationFilter;
import com.arsansys.RemaPartners.security.jwt.JwtUtils;

/**
 * Classe de configuració de seguretat.
 */
@Configuration
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    UserDetailsService userDetailsService;

    @Autowired
    JwtAutorizationFilter jwtAutorizationFilter;

    /**
     * Configura la cadena de filtres de seguretat.
     *
     * @param httpSecurity          Objecte HttpSecurity.
     * @param authenticationManager Objecte AuthenticationManager.
     * @return SecurityFilterChain cadena de filtres de seguretat configurada.
     * @throws Exception Excepció si hi ha algun error.
     */
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity, AuthenticationManager authenticationManager)
            throws Exception {

        JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(jwtUtils);
        jwtAuthenticationFilter.setAuthenticationManager(authenticationManager);

        return httpSecurity
                .csrf(config -> config.disable())
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers("/login", "/", "/api").permitAll();
                    // auth.anyRequest().authenticated();
                    auth.anyRequest().permitAll();
                })
                .sessionManagement(session -> {
                    session.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
                })
                .addFilter(jwtAuthenticationFilter)
                .addFilterBefore(jwtAutorizationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    /**
     * Bean que proporciona un codificador de contrasenyes BCrypt.
     *
     * @return BCryptPasswordEncoder Codificador de contrasenyes BCrypt.
     */
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Bean que proporciona un gestor d'autenticació.
     *
     * @param httpSecurity    Objecte HttpSecurity.
     * @param passwordEncoder Codificador de contrasenyes.
     * @return AuthenticationManager Gestor d'autenticació.
     * @throws Exception Excepció si hi ha algun error.
     */
    @SuppressWarnings("removal")
    @Bean
    AuthenticationManager authenticationManager(HttpSecurity httpSecurity, PasswordEncoder passwordEncoder)
            throws Exception {
        return httpSecurity.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder)
                .and().build();
    }

    /**
     * Mètode principal per a proves. Genera una contrasenya encriptada.
     *
     * @param args Arguments de la línia d'ordres.
     */
    // public static void main(String[] args) {
    // System.out.println(new BCryptPasswordEncoder().encode("1234"));
    // }

}

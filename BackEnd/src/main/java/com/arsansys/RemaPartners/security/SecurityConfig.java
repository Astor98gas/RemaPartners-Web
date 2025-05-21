package com.arsansys.RemaPartners.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.arsansys.RemaPartners.security.filters.JwtAuthenticationFilter;
import com.arsansys.RemaPartners.security.filters.JwtAutorizationFilter;
import com.arsansys.RemaPartners.security.jwt.JwtUtils;
import com.arsansys.RemaPartners.services.UserService;

import java.util.Arrays;

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

    @Autowired
    @Lazy
    UserService userService;

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

        JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(jwtUtils, userService);
        jwtAuthenticationFilter.setAuthenticationManager(authenticationManager);
        jwtAuthenticationFilter.setFilterProcessesUrl("/login");

        jwtAuthenticationFilter.setRequiresAuthenticationRequestMatcher(request -> {
            String path = request.getServletPath();
            return "/login".equals(path) && !"/logout".equals(path);
        });

        return httpSecurity
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(config -> config.disable())
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers("/login", "/logout", "/", "/api", "/createUser", "/vendedor/producto/getAll",
                            "/vendedor/producto/getById/*", "/api/binary-image/*",
                            "/api/images/*", "/resorces/static/**", "/api/stripe/**")
                            .permitAll();
                    auth.anyRequest().authenticated();
                    // auth.anyRequest().permitAll();
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
     * Bean de configuració CORS.
     *
     * @return CorsConfigurationSource Configuració CORS.
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:5173"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}

package com.arsansys.RemaPartners.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.arsansys.RemaPartners.controllers.dto.CreateUserDTO;
import com.arsansys.RemaPartners.models.entities.RolEntity;
import com.arsansys.RemaPartners.models.entities.UserEntity;
import com.arsansys.RemaPartners.models.enums.ERol;
import com.arsansys.RemaPartners.models.jwt.JwtResponse;
import com.arsansys.RemaPartners.security.jwt.JwtUtils;
import com.arsansys.RemaPartners.services.UserService;
import com.arsansys.RemaPartners.services.servicesImpl.UserDetailsServiceImpl;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Controlador para la gestión de autenticación JWT y operaciones relacionadas
 * con usuarios.
 * Permite crear usuarios, cerrar sesión, validar tokens y obtener información
 * de usuario.
 */
@CrossOrigin(origins = "http://localhost:5173")
@RestController
public class JwtController {

    @Autowired
    UserDetailsServiceImpl userDetailsServiceImpl;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtils jwtUtil;

    /**
     * Crea un nuevo usuario en el sistema.
     *
     * @param createUserDTO Datos del usuario a crear.
     * @param response      Respuesta HTTP para agregar cookies si es necesario.
     * @return Respuesta con el token JWT o mensaje de error.
     */
    @PostMapping("/createUser")
    public ResponseEntity<?> createUser(@Valid @RequestBody CreateUserDTO createUserDTO, HttpServletResponse response) {
        try {

            if (createUserDTO.getUsername() == null || createUserDTO.getUsername().isEmpty()) {
                return ResponseEntity.badRequest().body("Username is required");
            }
            if (createUserDTO.getEmail() == null || createUserDTO.getEmail().isEmpty()) {
                return ResponseEntity.badRequest().body("Email is required");
            }
            if (createUserDTO.getPassword() == null || createUserDTO.getPassword().isEmpty()) {
                return ResponseEntity.badRequest().body("Password is required");
            }

            // Create RolEntity from the role string
            RolEntity rolEntity = new RolEntity();
            rolEntity.setName(ERol.COMPRADOR);

            UserEntity userEntity = UserEntity.builder()
                    .username(createUserDTO.getUsername())
                    .password(createUserDTO.getPassword()) // El servicio se encargará de codificarlo
                    .email(createUserDTO.getEmail())
                    .googleToken(createUserDTO.getGoogleToken())
                    .rol(rolEntity)
                    .build();

            // Usar el servicio para crear el usuario y obtener el token
            JwtResponse jwtResponse = userService.createUser(userEntity);

            // Agregar el token como cookie (opcional, dependiendo de cómo quieras
            // manejarlo)
            Cookie cookie = new Cookie("token", jwtResponse.getToken());
            cookie.setMaxAge(Integer.MAX_VALUE);
            cookie.setPath("/");
            response.addCookie(cookie);

            // Devolver la respuesta con el token
            return ResponseEntity.ok(jwtResponse);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error creating user: " + e.getMessage());
        }
    }

    /**
     * Cierra la sesión del usuario invalidando el token JWT.
     *
     * @param request Solicitud HTTP para extraer el token.
     * @return Respuesta indicando el resultado del cierre de sesión.
     */
    @GetMapping("/log_out")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        try {
            String authHeader = request.getHeader("Authorization");
            String jwtToken = null;
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                jwtToken = authHeader.substring(7); // Quitar "Bearer " del inicio
            } else {
                Cookie[] cookies = request.getCookies();
                if (cookies != null) {
                    for (Cookie cookie : cookies) {
                        if ("token".equals(cookie.getName())) {
                            jwtToken = cookie.getValue();
                            break;
                        }
                    }
                }
            }

            // Invalidar el token
            if (jwtToken != null) {
                jwtUtil.invalidateToken(jwtToken);
            }

            return ResponseEntity.ok("Logout successful");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error during logout: " + e.getMessage());
        }
    }

    /**
     * Obtiene los detalles del usuario a partir de un token JWT.
     *
     * @param token Token JWT proporcionado.
     * @return Detalles del usuario o mensaje de error.
     */
    @GetMapping("/getUserByToken/{token}")
    public ResponseEntity<?> getUserByToken(@RequestParam String token) {
        try {
            // Validar el token
            if (jwtUtil.isTokenValid(token)) {
                String username = jwtUtil.getUsernameFromToken(token);
                UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername(username);

                // Si el token es válido y el usuario existe, devolver los detalles del usuario
                return ResponseEntity.ok(userDetails);
            } else {
                // Token inválido o expirado
                return ResponseEntity.status(401).body("Invalid or expired token");
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error retrieving user: " + e.getMessage());
        }
    }

    /**
     * Verifica si el usuario está autenticado mediante el token JWT.
     *
     * @param request Solicitud HTTP para extraer el token.
     * @return Información del usuario autenticado o mensaje de error.
     */
    @GetMapping("/isLoggedIn")
    public ResponseEntity<?> isLoggedIn(HttpServletRequest request) {
        try {
            // Buscar el token JWT en el encabezado de autorización
            String jwtToken = null;
            String authHeader = request.getHeader("Authorization");

            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                jwtToken = authHeader.substring(7); // Quitar "Bearer " del inicio
            }

            // Si no hay token, buscar en las cookies
            if (jwtToken == null) {
                Cookie[] cookies = request.getCookies();
                if (cookies != null) {
                    for (Cookie cookie : cookies) {
                        if ("token".equals(cookie.getName())) {
                            jwtToken = cookie.getValue();
                            break;
                        }
                    }
                }
            }

            // Si no hay token, el usuario no está logado
            if (jwtToken == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User is not logged in");
            }

            // Validar el token
            if (jwtUtil.isTokenValid(jwtToken)) {
                String username = jwtUtil.getUsernameFromToken(jwtToken);
                UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername(username);
                UserEntity userEntity = userService.getUserByUsername(userDetails.getUsername());

                return ResponseEntity.ok().body(userEntity);
            } else {
                // Token inválido o expirado
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired token");
            }
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error checking authentication status: " + e.getMessage());
        }
    }

}

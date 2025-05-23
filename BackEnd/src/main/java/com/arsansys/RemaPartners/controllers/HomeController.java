package com.arsansys.RemaPartners.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.arsansys.RemaPartners.controllers.dto.CreateUserDTO;
import com.arsansys.RemaPartners.models.entities.UserEntity;
import com.arsansys.RemaPartners.repositories.UserRepository;
import com.arsansys.RemaPartners.security.jwt.JwtUtils;
import com.arsansys.RemaPartners.services.UserService;

import org.springframework.web.bind.annotation.PathVariable;

/**
 * Controlador principal para operaciones generales y de usuario.
 * Permite gestionar usuarios y obtener información básica de la API.
 */
@CrossOrigin(origins = "http://localhost:5173")
@RestController
public class HomeController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtils jwtUtils;

    /**
     * Página de bienvenida de la API.
     *
     * @return Mensaje de bienvenida.
     */
    @GetMapping("/")
    public String home() {
        return "Welcome to the RemaPartners API!";
    }

    /**
     * Página de bienvenida alternativa de la API.
     *
     * @return Mensaje de bienvenida.
     */
    @GetMapping("/api")
    public String api() {
        return "Welcome to the RemaPartners API!";
    }

    /**
     * Obtiene la lista de todos los usuarios.
     *
     * @return Lista de usuarios.
     */
    @GetMapping("/getUsers")
    public ResponseEntity<List<UserEntity>> getUsers() {
        List<UserEntity> users = userService.getUsers();
        return ResponseEntity.ok(users);
    }

    /**
     * Obtiene un usuario por su nombre de usuario.
     *
     * @param username Nombre de usuario.
     * @return Usuario correspondiente.
     */
    @GetMapping("/getUserByUsername/{username}")
    public ResponseEntity<UserEntity> getUserByUsername(@PathVariable String username) {
        UserEntity user = userService.getUserByUsername(username);
        return ResponseEntity.ok(user);
    }

    /**
     * Obtiene un usuario por su ID.
     *
     * @param id ID del usuario.
     * @return Usuario correspondiente.
     */
    @GetMapping("/getUserById/{id}")
    public ResponseEntity<UserEntity> getUserById(@PathVariable String id) {
        UserEntity user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    /**
     * Obtiene un usuario por su correo electrónico.
     *
     * @param email Correo electrónico del usuario.
     * @return Usuario correspondiente.
     */
    @GetMapping("/getUserByEmail/{email}")
    public ResponseEntity<UserEntity> getUserByEmail(@PathVariable String email) {
        UserEntity user = userService.getByEmail(email);
        return ResponseEntity.ok(user);
    }

    /**
     * Elimina un usuario por su nombre de usuario.
     *
     * @param username Nombre de usuario.
     * @return Mensaje indicando el resultado de la operación.
     */
    @GetMapping("/deleteUser/{username}")
    public ResponseEntity<?> deleteUser(@PathVariable String username) {
        try {
            UserEntity user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            userRepository.delete(user);
            return ResponseEntity.ok().body("User deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error deleting user: " + e.getMessage());
        }
    }

    /**
     * Actualiza los datos de un usuario.
     *
     * @param id            ID del usuario a actualizar.
     * @param updateUserDTO Datos actualizados del usuario.
     * @param request       Solicitud HTTP para extraer el token si es necesario.
     * @return Mensaje indicando el resultado de la actualización.
     */
    @PostMapping("/updateUser/{id}")
    public ResponseEntity<?> updateUser(@PathVariable String id, @RequestBody CreateUserDTO updateUserDTO,
            HttpServletRequest request) {
        try {
            UserEntity user = userRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            // Check if username is changing
            boolean usernameChanged = !user.getUsername().equals(updateUserDTO.getUsername());

            // Actualizar datos básicos
            user.setUsername(updateUserDTO.getUsername());
            user.setEmail(updateUserDTO.getEmail());

            // Comprueba si la contraseña ha cambiado para encriptarla
            if (updateUserDTO.getPassword() != null && !updateUserDTO.getPassword().equals(user.getPassword())) {
                user.setPassword(passwordEncoder.encode(updateUserDTO.getPassword()));
            }

            // Actualizar los nuevos campos de perfil
            if (updateUserDTO.getDescription() != null) {
                user.setDescription(updateUserDTO.getDescription());
            }

            if (updateUserDTO.getProfileImage() != null) {
                user.setProfileImage(updateUserDTO.getProfileImage());
            }

            // Save updated user
            userRepository.save(user);

            // If username changed, invalidate the JWT token
            if (usernameChanged) {
                // Extract JWT token from request
                String authHeader = request.getHeader("Authorization");
                if (authHeader != null && authHeader.startsWith("Bearer ")) {
                    String token = authHeader.substring(7);
                    // Use the jwtUtils to invalidate the token
                    jwtUtils.invalidateToken(token);

                    // Return special response to indicate token invalidation
                    Map<String, Object> response = new HashMap<>();
                    response.put("usernameChanged", true);
                    response.put("message", "User updated successfully. Please login again with new username.");
                    return ResponseEntity.ok(response);
                }
            }

            return ResponseEntity.ok("User updated successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Obtiene el perfil público de un usuario por su ID.
     *
     * @param id ID del usuario.
     * @return Perfil público del usuario.
     */
    @GetMapping("/getUserProfileById/{id}")
    public ResponseEntity<?> getUserProfileById(@PathVariable String id) {
        try {
            UserEntity user = userRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            // For security reasons, we don't want to expose passwords and other sensitive
            // data in public profiles
            user.setPassword(null);
            user.setGoogleToken(null);

            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error getting user profile: " + e.getMessage());
        }
    }
}

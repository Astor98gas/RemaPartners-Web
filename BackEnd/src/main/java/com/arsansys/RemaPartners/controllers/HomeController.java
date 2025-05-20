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

    @GetMapping("/")
    public String home() {
        return "Welcome to the RemaPartners API!";
    }

    @GetMapping("/api")
    public String api() {
        return "Welcome to the RemaPartners API!";
    }

    @GetMapping("/getUsers")
    public ResponseEntity<List<UserEntity>> getUsers() {
        List<UserEntity> users = userService.getUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/getUserByUsername/{username}")
    public ResponseEntity<UserEntity> getUserByUsername(@PathVariable String username) {
        UserEntity user = userService.getUserByUsername(username);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/getUserById/{id}")
    public ResponseEntity<UserEntity> getUserById(@PathVariable String id) {
        UserEntity user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/getUserByEmail/{email}")
    public ResponseEntity<UserEntity> getUserByEmail(@PathVariable String email) {
        UserEntity user = userService.getByEmail(email);
        return ResponseEntity.ok(user);
    }

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

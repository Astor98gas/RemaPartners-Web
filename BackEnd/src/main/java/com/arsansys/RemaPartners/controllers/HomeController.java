package com.arsansys.RemaPartners.controllers;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.arsansys.RemaPartners.controllers.dto.CreateUserDTO;
import com.arsansys.RemaPartners.models.entities.RolEntity;
import com.arsansys.RemaPartners.models.entities.UserEntity;
import com.arsansys.RemaPartners.models.enums.ERol;
import com.arsansys.RemaPartners.models.jwt.JwtResponse;
import com.arsansys.RemaPartners.repositories.UserRepository;
import com.arsansys.RemaPartners.services.UserService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestParam;
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

    // public HomeController(UserRepository userRepository, PasswordEncoder
    // passwordEncoder) {
    // this.userRepository = userRepository;
    // this.passwordEncoder = passwordEncoder;
    // }

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
    public ResponseEntity<?> updateUser(@PathVariable String id, @RequestBody CreateUserDTO updateUserDTO) {
        try {
            UserEntity user = userRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            user.setUsername(updateUserDTO.getUsername());
            user.setEmail(updateUserDTO.getEmail());
            if (updateUserDTO.getPassword() != user.getPassword()) {
                user.setPassword(passwordEncoder.encode(updateUserDTO.getPassword()));
            }

            userRepository.save(user);
            return ResponseEntity.ok().body("User updated successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error updating user: " + e.getMessage());
        }
    }

}

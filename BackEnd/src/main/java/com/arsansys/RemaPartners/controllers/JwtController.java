package com.arsansys.RemaPartners.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.arsansys.RemaPartners.controllers.dto.CreateUserDTO;
import com.arsansys.RemaPartners.models.entities.RolEntity;
import com.arsansys.RemaPartners.models.entities.UserEntity;
import com.arsansys.RemaPartners.models.enums.ERol;
import com.arsansys.RemaPartners.models.jwt.JwtRequest;
import com.arsansys.RemaPartners.models.jwt.JwtResponse;
import com.arsansys.RemaPartners.security.jwt.JwtUtils;
import com.arsansys.RemaPartners.services.UserDetailsServiceImpl;
import com.arsansys.RemaPartners.services.UserService;
import com.google.cloud.storage.Acl.User;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
public class JwtController {

    @Autowired
    UserDetailsServiceImpl userDetailsServiceImpl;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtils jwtUtil;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/login") // when trying this url,select auth type: No Auth
    public String generateToken(Model m, HttpSession session,
            @RequestBody JwtRequest jwtRequest, HttpServletResponse res) throws Exception {
        System.out.println(jwtRequest);
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(jwtRequest.getUsername(), jwtRequest.getPassword()));
        } catch (UsernameNotFoundException e) {

            session.setAttribute("msg", "Bad Credentials");
            return "redirect:/login";

        } catch (BadCredentialsException e) {
            session.setAttribute("msg", "Bad Credentials");
            return "redirect:/login";
        }

        try {

            final UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername(jwtRequest.getUsername());

            System.out.println("userDetails.getUsername: " + userDetails.getUsername());

            final String token = jwtUtil.generateAccesToken(userDetails.getUsername());

            Cookie cookie = new Cookie("token", token);
            cookie.setMaxAge(Integer.MAX_VALUE);
            res.addCookie(cookie);

            System.out.println("token: " + token);

            return "redirect:/";
        } catch (Exception e) {
            session.setAttribute("msg", "Credentials were right But something went wrong!!");
            return "redirect:/login";
        }
    }

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
            if (createUserDTO.getRol() == null || createUserDTO.getRol().isEmpty()) {
                createUserDTO.setRol(ERol.COMPRADOR.name());
            }
            // Create RolEntity from the role string
            RolEntity rolEntity = new RolEntity();
            rolEntity.setName(ERol.valueOf(createUserDTO.getRol()));

            UserEntity userEntity = UserEntity.builder()
                    .username(createUserDTO.getUsername())
                    .password(createUserDTO.getPassword()) // El servicio se encargará de codificarlo
                    .email(createUserDTO.getEmail())
                    // .nombre(createUserDTO.getNom())
                    // .apellidos(createUserDTO.getCognoms())
                    // .dni(createUserDTO.getDni())
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

    @GetMapping("/log_out")
    public String logout(HttpServletRequest request, HttpServletResponse res, Model m, HttpSession session) {

        String msg = null;

        Cookie[] cookies2 = request.getCookies();
        for (int i = 0; i < cookies2.length; i++) {
            if (cookies2[i].getName().equals("token")) {
                cookies2[i].setMaxAge(0);
                res.addCookie(cookies2[i]);
                msg = "Logout successfully";

            }

        }
        session.setAttribute("msg", msg);

        return "redirect:/login";
    }

    @GetMapping("/isLoggedIn")
    public ResponseEntity<?> isLoggedIn(HttpServletRequest request) {
        try {
            // Buscar el token JWT en las cookies y en el encabezado de autorización
            String jwtToken = null;

            // Primero intentar obtener el token de las cookies
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if ("token".equals(cookie.getName())) {
                        jwtToken = cookie.getValue();
                        break;
                    }
                }
            }

            // Si no se encuentra en las cookies, buscar en el encabezado Authorization
            if (jwtToken == null) {
                String authHeader = request.getHeader("Authorization");
                if (authHeader != null && authHeader.startsWith("Bearer ")) {
                    jwtToken = authHeader.substring(7); // Quitar "Bearer " del inicio
                }
            }

            // Si no hay token, el usuario no está logado
            if (jwtToken == null) {
                return ResponseEntity.ok().body("User is not logged in");
            }

            // Validar el token
            if (jwtUtil.isTokenValid(jwtToken)) {
                String username = jwtUtil.getUsernameFromToken(jwtToken);
                UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername(username);

                // Si el token es válido y el usuario existe, está logado
                return ResponseEntity.ok().body("User is logged in");
            } else {
                // Token inválido o expirado
                return ResponseEntity.ok().body("Invalid or expired token");
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error checking authentication status: " + e.getMessage());
        }
    }

}

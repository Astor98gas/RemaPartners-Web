package com.arsansys.RemaPartners.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.arsansys.RemaPartners.models.entities.UserEntity;
import com.arsansys.RemaPartners.models.jwt.JwtRequest;
import com.arsansys.RemaPartners.security.jwt.JwtUtils;
import com.arsansys.RemaPartners.services.UserDetailsServiceImpl;
import com.arsansys.RemaPartners.services.UserService;
import com.google.cloud.storage.Acl.User;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.RequestBody;

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

    @PostMapping("/user/createUser")
    public String createUser(@RequestBody UserEntity userEntity) {
        if (userEntity.getUsername() == null || userEntity.getPassword() == null) {
            return "Username and password are required";
        }
        try {
            userService.createUser(userEntity);
            return "User created successfully";
        } catch (Exception e) {
            return "Error creating user: " + e.getMessage();
        }
    }

    @PostMapping("/token") // when trying this url,select auth type: No Auth
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

            return "redirect:/user/";
        } catch (Exception e) {
            session.setAttribute("msg", "Credentials were right But something went wrong!!");
            return "redirect:/login";
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

}

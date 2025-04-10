package com.arsansys.RemaPartners.services.servicesImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.arsansys.RemaPartners.models.entities.UserEntity;
import com.arsansys.RemaPartners.repositories.UserRepository;
import com.arsansys.RemaPartners.services.UserService;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public List<UserEntity> getUsers() {
        userRepository.findAll();
        return userRepository.findAll();
    }

    public void createUser(UserEntity userEntity) {
        try {
            // Check if the user already exists
            if (userRepository.findByUsername(userEntity.getUsername()).isPresent()) {
                throw new RuntimeException("User already exists");
            }
            // Hash the password before saving
            String hashedPassword = passwordEncoder.encode(userEntity.getPassword());
            userEntity.setPassword(hashedPassword);
            userRepository.save(userEntity);
        } catch (Exception e) {
            throw new RuntimeException("Error creating user: " + e.getMessage());
        }
    }

    @Override
    public UserEntity getUserById(String id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public UserEntity getUserByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }

}

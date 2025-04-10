package com.arsansys.RemaPartners.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.arsansys.RemaPartners.models.entities.UserEntity;
import com.arsansys.RemaPartners.models.jwt.JwtResponse;

@Service
public interface UserService {

    abstract List<UserEntity> getUsers();

    abstract UserEntity getUserById(String id);

    abstract UserEntity getUserByUsername(String username);

    abstract JwtResponse createUser(UserEntity userEntity);

}

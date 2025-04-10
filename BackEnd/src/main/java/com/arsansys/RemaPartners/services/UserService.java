package com.arsansys.RemaPartners.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.arsansys.RemaPartners.models.entities.UserEntity;

@Service
public interface UserService {

    abstract List<UserEntity> getUsers();

    abstract UserEntity getUserById(String id);

    abstract UserEntity getUserByUsername(String username);

    abstract void createUser(UserEntity userEntity);

}

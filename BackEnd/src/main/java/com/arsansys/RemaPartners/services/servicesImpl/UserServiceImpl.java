package com.arsansys.RemaPartners.services.servicesImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.arsansys.RemaPartners.models.entities.UserEntity;
import com.arsansys.RemaPartners.repositories.UserRepository;
import com.arsansys.RemaPartners.services.UserService;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<UserEntity> getUsers() {
        userRepository.findAll();
        return userRepository.findAll();
    }

}

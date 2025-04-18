package com.arsansys.RemaPartners.services;

import org.springframework.stereotype.Service;

import com.arsansys.RemaPartners.models.entities.JwtEntity;

@Service
public interface JwtService {

    JwtEntity findByUsername(String username);

    JwtEntity findByToken(String token);

    void save(JwtEntity jwtToken);

}

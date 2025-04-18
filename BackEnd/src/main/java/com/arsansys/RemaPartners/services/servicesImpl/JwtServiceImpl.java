package com.arsansys.RemaPartners.services.servicesImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.arsansys.RemaPartners.models.entities.JwtEntity;
import com.arsansys.RemaPartners.repositories.JwtRepository;
import com.arsansys.RemaPartners.services.JwtService;

@Service
public class JwtServiceImpl implements JwtService {

    @Autowired
    private JwtRepository jwtRepository;

    @Override
    public JwtEntity findByUsername(String username) {
        return jwtRepository.findByUsername(username);
    }

    @Override
    public JwtEntity findByToken(String token) {
        return jwtRepository.findByToken(token);
    }

    @Override
    public void save(JwtEntity jwtToken) {
        jwtRepository.save(jwtToken);
    }
}

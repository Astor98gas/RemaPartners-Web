package com.arsansys.RemaPartners.services.stripe;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.arsansys.RemaPartners.models.entities.Suscripcion;
import com.arsansys.RemaPartners.repositories.SuscripcionRepository;

@Service
public class SuscripcionService {

    @Autowired
    SuscripcionRepository suscripcionRepository;

    public List<Suscripcion> getAllSuscripciones() {
        return suscripcionRepository.findAll();
    }

    public Optional<Suscripcion> getSuscripcionById(String id) {
        return Optional.of(suscripcionRepository.findById(id).orElse(null));
    }

}

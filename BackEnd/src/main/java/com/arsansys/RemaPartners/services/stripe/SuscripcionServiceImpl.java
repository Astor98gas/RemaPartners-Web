package com.arsansys.RemaPartners.services.stripe;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.arsansys.RemaPartners.models.entities.Suscripcion;
import com.arsansys.RemaPartners.repositories.SuscripcionRepository;

/**
 * Implementación del servicio de suscripciones.
 */
@Service
public class SuscripcionServiceImpl implements SuscripcionService {

    @Autowired
    private SuscripcionRepository suscripcionRepository;

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Suscripcion> getSuscripcionesByIdUsuario(String idUsuario) {
        return suscripcionRepository.findByIdUsuario(idUsuario);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Suscripcion createSuscripcion(String isUsuario) {
        Suscripcion suscripcion = new Suscripcion();
        suscripcion.setIdUsuario(isUsuario);
        // Set other default values if needed
        return suscripcionRepository.save(suscripcion);
    }

}

package com.arsansys.RemaPartners.services.stripe;

import java.util.List;

import org.springframework.stereotype.Service;

import com.arsansys.RemaPartners.models.entities.Suscripcion;

@Service
public interface SuscripcionService {

    abstract List<Suscripcion> getSuscripcionesByIdUsuario(String idUsuario);

    abstract Suscripcion createSuscripcion(String isUsuario);

}

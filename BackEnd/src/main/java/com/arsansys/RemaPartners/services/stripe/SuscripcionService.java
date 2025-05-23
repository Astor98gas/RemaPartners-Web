package com.arsansys.RemaPartners.services.stripe;

import java.util.List;

import org.springframework.stereotype.Service;

import com.arsansys.RemaPartners.models.entities.Suscripcion;

/**
 * Servicio para la gestión de suscripciones.
 */
@Service
public interface SuscripcionService {

    /**
     * Obtiene la lista de suscripciones asociadas a un usuario por su ID.
     *
     * @param idUsuario el identificador del usuario
     * @return lista de suscripciones del usuario
     */
    abstract List<Suscripcion> getSuscripcionesByIdUsuario(String idUsuario);

    /**
     * Crea una nueva suscripción para un usuario.
     *
     * @param isUsuario el identificador del usuario
     * @return la suscripción creada
     */
    abstract Suscripcion createSuscripcion(String isUsuario);

}

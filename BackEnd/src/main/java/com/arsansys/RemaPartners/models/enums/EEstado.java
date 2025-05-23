package com.arsansys.RemaPartners.models.enums;

/**
 * Enum que representa los posibles estados de un objeto.
 * Cada estado tiene una descripción asociada.
 */
public enum EEstado {
    NUEVO("Nuevo"),
    COMONUEVO("Como nuevo"),
    BUENESTADO("Buen estado"),
    ACEPTABLE("Aceptable"),
    USADO("Usado"),
    REPARADO("Reparado"),
    REPARACIONESNECESARIAS("Reparaciones necesarias"),
    DESCONOCIDO("Desconocido");

    /**
     * Descripción legible del estado.
     */
    private final String estado;

    /**
     * Constructor del enum EEstado.
     * 
     * @param estado Descripción legible del estado.
     */
    EEstado(String estado) {
        this.estado = estado;
    }

    /**
     * Obtiene la descripción legible del estado.
     * 
     * @return Descripción del estado.
     */
    public String getEstado() {
        return estado;
    }

    /**
     * Obtiene el enum EEstado correspondiente a una cadena de texto.
     * 
     * @param estado Cadena con la descripción del estado.
     * @return EEstado correspondiente.
     * @throws IllegalArgumentException Si no se encuentra el estado.
     */
    public static EEstado fromString(String estado) {
        for (EEstado eEstado : EEstado.values()) {
            if (eEstado.estado.equalsIgnoreCase(estado)) {
                return eEstado;
            }
        }
        throw new IllegalArgumentException("No se encontró el estado: " + estado);
    }

}

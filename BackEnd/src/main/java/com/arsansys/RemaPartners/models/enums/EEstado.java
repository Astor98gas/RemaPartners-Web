package com.arsansys.RemaPartners.models.enums;

public enum EEstado {
    NUEVO("Nuevo"),
    COMONUEVO("Como nuevo"),
    BUENESTADO("Buen estado"),
    ACEPTABLE("Aceptable"),
    USADO("Usado"),
    REPARADO("Reparado"),
    REPARACIONESNECESARIAS("Reparaciones necesarias"),
    DESCONOCIDO("Desconocido");

    private final String estado;

    EEstado(String estado) {
        this.estado = estado;
    }

    public String getEstado() {
        return estado;
    }

    public static EEstado fromString(String estado) {
        for (EEstado eEstado : EEstado.values()) {
            if (eEstado.estado.equalsIgnoreCase(estado)) {
                return eEstado;
            }
        }
        throw new IllegalArgumentException("No se encontró el estado: " + estado);
    }

}

package com.arsansys.RemaPartners.models.enums;

/**
 * Enum que representa diferentes tipos de monedas.
 * Cada moneda tiene una descripción asociada.
 */
public enum EMoneda {

    EUR("Euro"),
    USD("Dólar estadounidense"),
    GBP("Libra esterlina"),
    JPY("Yen japonés"),
    CNY("Yuan chino"),
    INR("Rupia india"),
    RUB("Rublo ruso"),
    BRL("Real brasileño"),
    ARS("Peso argentino"),
    CLP("Peso chileno");

    /**
     * Descripción legible de la moneda.
     */
    private final String moneda;

    /**
     * Constructor del enum EMoneda.
     * 
     * @param moneda Descripción legible de la moneda.
     */
    EMoneda(String moneda) {
        this.moneda = moneda;
    }

    /**
     * Obtiene la descripción legible de la moneda.
     * 
     * @return Descripción de la moneda.
     */
    public String getMoneda() {
        return moneda;
    }

    /**
     * Obtiene el enum EMoneda correspondiente a una cadena de texto.
     * 
     * @param moneda Cadena con la descripción de la moneda.
     * @return EMoneda correspondiente.
     * @throws IllegalArgumentException Si no se encuentra la moneda.
     */
    public static EMoneda fromString(String moneda) {
        for (EMoneda eMoneda : EMoneda.values()) {
            if (eMoneda.moneda.equalsIgnoreCase(moneda)) {
                return eMoneda;
            }
        }
        throw new IllegalArgumentException("No se encontró la moneda: " + moneda);
    }

}

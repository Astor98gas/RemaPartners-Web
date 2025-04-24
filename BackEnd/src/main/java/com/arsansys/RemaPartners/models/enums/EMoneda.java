package com.arsansys.RemaPartners.models.enums;

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

    private final String moneda;

    EMoneda(String moneda) {
        this.moneda = moneda;
    }

    public String getMoneda() {
        return moneda;
    }

    public static EMoneda fromString(String moneda) {
        for (EMoneda eMoneda : EMoneda.values()) {
            if (eMoneda.moneda.equalsIgnoreCase(moneda)) {
                return eMoneda;
            }
        }
        throw new IllegalArgumentException("No se encontró la moneda: " + moneda);
    }

}

package com.arsansys.RemaPartners.services;

public interface CurrencyConversionService {
    /**
     * Convierte una cantidad de una moneda origen a una moneda destino.
     *
     * @param amount         Cantidad en centavos de la moneda origen.
     * @param sourceCurrency Código de la moneda origen.
     * @param targetCurrency Código de la moneda destino.
     * @return Cantidad en centavos de la moneda destino.
     */
    int convertCurrency(int amount, String sourceCurrency, String targetCurrency);
}

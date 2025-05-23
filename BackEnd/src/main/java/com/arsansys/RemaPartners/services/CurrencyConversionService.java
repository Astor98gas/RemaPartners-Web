package com.arsansys.RemaPartners.services;

public interface CurrencyConversionService {
    /**
     * Converts an amount from source currency to target currency
     *
     * @param amount         Amount in source currency cents
     * @param sourceCurrency Source currency code
     * @param targetCurrency Target currency code
     * @return Amount in target currency cents
     */
    int convertCurrency(int amount, String sourceCurrency, String targetCurrency);
}

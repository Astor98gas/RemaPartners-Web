package com.arsansys.RemaPartners.controllers.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.access.prepost.PreAuthorize;

import com.arsansys.RemaPartners.services.servicesImpl.CurrencyConversionServiceImpl;

import java.util.HashMap;
import java.util.Map;

/**
 * Controlador REST para operaciones relacionadas con monedas y tasas de cambio.
 * Proporciona endpoints para consultar tasas actuales, forzar actualización de
 * tasas
 * y probar conversiones de moneda.
 */
@RestController
@RequestMapping("/api/currency")
public class CurrencyController {

    /**
     * Servicio para la conversión y actualización de tasas de cambio.
     */
    @Autowired
    private CurrencyConversionServiceImpl currencyService;

    /**
     * Obtiene las tasas de cambio actuales.
     *
     * @return ResponseEntity con un mapa de códigos de moneda y sus tasas.
     */
    @GetMapping("/rates")
    public ResponseEntity<Map<String, Double>> getCurrentRates() {
        return ResponseEntity.ok(currencyService.getCurrentRates());
    }

    /**
     * Fuerza la actualización de las tasas de cambio.
     * Solo accesible para usuarios con rol ADMIN.
     *
     * @return ResponseEntity con mensaje de confirmación.
     */
    @PostMapping("/update-rates")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> updateRates() {
        currencyService.forceUpdateRates();
        return ResponseEntity.ok("Exchange rates update initiated");
    }

    /**
     * Endpoint de prueba para realizar conversiones conocidas entre monedas.
     *
     * @return ResponseEntity con un mapa de conversiones de ejemplo.
     */
    @GetMapping("/test-conversion")
    public ResponseEntity<Map<String, Object>> testConversion() {
        Map<String, Object> result = new HashMap<>();

        // Prueba de conversiones conocidas
        result.put("100USD_to_EUR", currencyService.convertCurrency(10000, "USD", "EUR"));
        result.put("100EUR_to_USD", currencyService.convertCurrency(10000, "EUR", "USD"));
        result.put("100GBP_to_EUR", currencyService.convertCurrency(10000, "GBP", "EUR"));

        return ResponseEntity.ok(result);
    }
}
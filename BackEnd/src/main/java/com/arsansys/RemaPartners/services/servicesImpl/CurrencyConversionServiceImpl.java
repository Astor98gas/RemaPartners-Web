package com.arsansys.RemaPartners.services.servicesImpl;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import com.arsansys.RemaPartners.services.CurrencyConversionService;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.io.StringReader;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;

/**
 * Implementación del servicio para la conversión de divisas utilizando tasas de
 * cambio del BCE.
 */
@Service
public class CurrencyConversionServiceImpl implements CurrencyConversionService {
    private static final Logger logger = LoggerFactory.getLogger(CurrencyConversionServiceImpl.class);

    // ECB Exchange rate API URL
    private static final String ECB_EXCHANGE_RATE_URL = "https://www.ecb.europa.eu/stats/eurofxref/eurofxref-daily.xml";

    // Thread-safe map to store exchange rates
    private final Map<String, Double> exchangeRates = new ConcurrentHashMap<>();

    // Initialize with default values (used as fallback)
    /**
     * Inicializa las tasas de cambio por defecto y realiza una actualización
     * inicial.
     */
    @PostConstruct
    public void init() {
        // Set EUR as base currency
        exchangeRates.put("EUR", 1.0);

        // Set some default values as fallback
        exchangeRates.put("USD", 0.91);
        exchangeRates.put("GBP", 1.17);
        exchangeRates.put("JPY", 0.0062);
        exchangeRates.put("CNY", 0.13);
        exchangeRates.put("INR", 0.011);
        exchangeRates.put("RUB", 0.0096);
        exchangeRates.put("BRL", 0.18);
        exchangeRates.put("ARS", 0.0011);
        exchangeRates.put("CLP", 0.00097);

        // Fetch rates immediately on startup
        updateExchangeRates();
    }

    // Update exchange rates daily at midnight
    /**
     * Actualiza las tasas de cambio diariamente a medianoche.
     */
    @Scheduled(cron = "0 0 0 * * ?")
    public void scheduledRateUpdate() {
        updateExchangeRates();
    }

    // Method to fetch exchange rates from ECB
    private void updateExchangeRates() {
        try {
            RestTemplate restTemplate = new RestTemplate();
            String xmlResponse = restTemplate.getForObject(ECB_EXCHANGE_RATE_URL, String.class);

            if (xmlResponse != null) {
                parseXmlExchangeRates(xmlResponse);
                logger.info("Exchange rates updated successfully from ECB");
            }
        } catch (Exception e) {
            logger.error("Error updating exchange rates from ECB: {}", e.getMessage());
        }
    }

    // Parse the XML response from ECB
    private void parseXmlExchangeRates(String xmlResponse) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new InputSource(new StringReader(xmlResponse)));

            NodeList cubeNodes = doc.getElementsByTagName("Cube");

            for (int i = 0; i < cubeNodes.getLength(); i++) {
                Element cube = (Element) cubeNodes.item(i);

                // Check if this Cube element has currency and rate attributes
                if (cube.hasAttribute("currency") && cube.hasAttribute("rate")) {
                    String currency = cube.getAttribute("currency");
                    double rate = Double.parseDouble(cube.getAttribute("rate"));

                    // Store in our map (1 EUR = rate CURRENCY)
                    exchangeRates.put(currency, 1.0 / rate);
                    logger.debug("Updated rate for {}: {}", currency, rate);
                }
            }
        } catch (Exception e) {
            logger.error("Error parsing XML exchange rates: {}", e.getMessage());
        }
    }

    // Force an update of exchange rates (can be called via API if needed)
    /**
     * Fuerza una actualización de las tasas de cambio desde el BCE.
     */
    public void forceUpdateRates() {
        updateExchangeRates();
    }

    /**
     * Convierte una cantidad de una moneda origen a una moneda destino.
     * 
     * @param amount         Cantidad a convertir (en céntimos).
     * @param targetCurrency Moneda destino.
     * @param sourceCurrency Moneda origen.
     * @return Cantidad convertida (en céntimos).
     */
    @Override
    public int convertCurrency(int amount, String targetCurrency, String sourceCurrency) {
        if (sourceCurrency.equals(targetCurrency)) {
            return amount;
        }

        // Get exchange rates (where values are: 1 EUR = X currency)
        double sourceRate = exchangeRates.getOrDefault(sourceCurrency, 1.0);
        double targetRate = exchangeRates.getOrDefault(targetCurrency, 1.0);

        // CORRECTED FORMULA:
        // 1. Convert from source currency to EUR: amount * (1/sourceRate)
        // 2. Convert from EUR to target currency: amountInEur * targetRate
        double amountInEur = amount * (1.0 / sourceRate);
        double amountInTarget = amountInEur * targetRate;

        return (int) Math.round(amountInTarget);
    }

    /**
     * Obtiene las tasas de cambio actuales.
     * 
     * @return Mapa con las tasas de cambio actuales.
     */
    public Map<String, Double> getCurrentRates() {
        return new HashMap<>(exchangeRates);
    }
}
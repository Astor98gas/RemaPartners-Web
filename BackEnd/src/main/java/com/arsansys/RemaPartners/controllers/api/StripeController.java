package com.arsansys.RemaPartners.controllers.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.arsansys.RemaPartners.services.UserService;
import com.arsansys.RemaPartners.services.stripe.StripeService;
import com.arsansys.RemaPartners.services.stripe.SuscripcionService;
import com.stripe.Stripe;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/stripe/")
@CrossOrigin(origins = "*") // Asegúrate de que esta línea esté presente
public class StripeController {

    @Autowired
    private SuscripcionService suscripcionService;

    @Autowired
    private StripeService stripeService;

    @Autowired
    private UserService userService;

    @Value("${stripe.api.secret}")
    private String stripeApiKey;

    @GetMapping("/suscripciones")
    public ResponseEntity<?> getAllSuscripciones() {
        try {
            return ResponseEntity.ok(suscripcionService.getAllSuscripciones());
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @PostMapping("/create-checkout-session")
    public ResponseEntity<Map<String, String>> createCheckoutSession(@RequestBody Map<String, Object> requestData) {
        try {
            Stripe.apiKey = stripeApiKey;

            // Imprimir datos recibidos para depuración
            System.out.println("Datos recibidos: " + requestData);

            String priceId = (String) requestData.get("priceId");
            String successUrl = (String) requestData.get("successUrl");
            String cancelUrl = (String) requestData.get("cancelUrl");
            String clientReferenceId = (String) requestData.get("clientReferenceId");

            // Validar datos requeridos
            if (priceId == null || successUrl == null || cancelUrl == null) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Faltan datos requeridos: priceId, successUrl o cancelUrl");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
            }

            // Asegurar que clientReferenceId nunca sea null
            if (clientReferenceId == null) {
                clientReferenceId = "default_user";
            }

            SessionCreateParams params = SessionCreateParams.builder()
                    .setMode(SessionCreateParams.Mode.PAYMENT)
                    .setClientReferenceId(clientReferenceId)
                    .setSuccessUrl(successUrl)
                    .setCancelUrl(cancelUrl)
                    .addLineItem(
                            SessionCreateParams.LineItem.builder()
                                    .setPrice(priceId)
                                    .setQuantity(1L)
                                    .build())
                    .build();

            Session session = Session.create(params);

            Map<String, String> response = new HashMap<>();
            response.put("url", session.getUrl());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            // Imprimir el stack trace completo para depuración
            e.printStackTrace();

            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            error.put("stackTrace", e.getStackTrace().toString());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
}

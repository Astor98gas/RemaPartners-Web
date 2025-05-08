package com.arsansys.RemaPartners.controllers.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.arsansys.RemaPartners.controllers.dto.PaymentIntentDTO;
import com.arsansys.RemaPartners.models.entities.UserEntity;
import com.arsansys.RemaPartners.services.UserService;
import com.arsansys.RemaPartners.services.stripe.StripeService;
import com.arsansys.RemaPartners.services.stripe.SuscripcionService;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
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

    // @Value("${stripe.webhook.secret}")
    // private String webhookSecret;

    @GetMapping("/suscripciones")
    public ResponseEntity<?> getAllSuscripciones() {
        try {
            return ResponseEntity.ok(suscripcionService.getAllSuscripciones());
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @PostMapping("/paymentintent")
    public ResponseEntity<String> createPaymentIntent(@RequestBody PaymentIntentDTO paymentIntentDTO) {
        try {
            PaymentIntent paymentIntent = stripeService.paymentIntent(paymentIntentDTO);
            String paymentString = paymentIntent.toJson();
            return ResponseEntity.ok(paymentString);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @PostMapping("/confirm/{paymentIntentId}")
    public ResponseEntity<String> confirmPaymentIntent(@RequestBody String paymentIntentId) {
        try {
            PaymentIntent paymentIntent = stripeService.confirmPaymentIntent(paymentIntentId);
            String paymentString = paymentIntent.toJson();
            return ResponseEntity.ok(paymentString);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @PostMapping("/cancel/{paymentIntentId}")
    public ResponseEntity<String> cancelPaymentIntent(@RequestBody String paymentIntentId) {
        try {
            PaymentIntent paymentIntent = stripeService.cancelPaymentIntent(paymentIntentId);
            String paymentString = paymentIntent.toJson();
            return ResponseEntity.ok(paymentString);
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

    // // Webhook para recibir eventos de Stripe
    // @PostMapping("/webhook")
    // public ResponseEntity<?> handleStripeWebhook(
    // @RequestBody String payload,
    // @RequestHeader("Stripe-Signature") String sigHeader) {
    // try {
    // // Procesar el evento recibido de Stripe
    // String event = stripeService.handleWebhookEvent(payload, sigHeader,
    // webhookSecret);

    // return ResponseEntity.ok(event);
    // } catch (StripeException e) {
    // return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    // }
    // }
}

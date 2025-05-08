package com.arsansys.RemaPartners.controllers.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.arsansys.RemaPartners.controllers.dto.PaymentIntentDTO;
import com.arsansys.RemaPartners.services.stripe.StripeService;
import com.arsansys.RemaPartners.services.stripe.SuscripcionService;
import com.stripe.model.PaymentIntent;

@RestController
@RequestMapping("/api/stripe")
public class StripeController {

    @Autowired
    private SuscripcionService suscripcionService;

    @Autowired
    private StripeService stripeService;

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

}

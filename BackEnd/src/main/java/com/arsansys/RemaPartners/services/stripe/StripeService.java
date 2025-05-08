package com.arsansys.RemaPartners.services.stripe;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.arsansys.RemaPartners.controllers.dto.PaymentIntentDTO;
import com.arsansys.RemaPartners.models.entities.UserEntity;
import com.arsansys.RemaPartners.repositories.UserRepository;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.EventDataObjectDeserializer;
import com.stripe.model.PaymentIntent;
import com.stripe.model.StripeObject;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;

@Service
public class StripeService {

    @Value("${stripe.api.secret}")
    private String stripeApiKey;

    @Autowired
    private UserRepository userRepository;

    public PaymentIntent paymentIntent(PaymentIntentDTO paymentIntentDTO) throws StripeException {
        Stripe.apiKey = stripeApiKey;

        Map<String, Object> params = new HashMap<>();
        params.put("amount", paymentIntentDTO.getAmount());
        params.put("currency", paymentIntentDTO.getCurrency());
        params.put("description", paymentIntentDTO.getDescription());

        try {
            PaymentIntent paymentIntent = PaymentIntent.create(params);
            return paymentIntent;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public PaymentIntent confirmPaymentIntent(String paymentIntentId) throws StripeException {
        Stripe.apiKey = stripeApiKey;

        Map<String, Object> params = new HashMap<>();
        params.put("payment_intent", paymentIntentId);
        params.put("payment_method", "pm_card_visa");

        try {
            PaymentIntent paymentIntent = PaymentIntent.retrieve(paymentIntentId);
            paymentIntent.confirm(params);
            return paymentIntent;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public PaymentIntent cancelPaymentIntent(String paymentIntentId) throws StripeException {
        Stripe.apiKey = stripeApiKey;

        try {
            PaymentIntent paymentIntent = PaymentIntent.retrieve(paymentIntentId);
            paymentIntent.cancel();
            return paymentIntent;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // // Método para manejar webhooks de Stripe
    // public String handleWebhookEvent(String payload, String sigHeader, String
    // webhookSecret) throws StripeException {
    // Stripe.apiKey = stripeApiKey;

    // try {
    // // Construir el evento a partir del payload y la firma
    // Event event = Webhook.constructEvent(payload, sigHeader, webhookSecret);

    // // Procesar el evento según su tipo
    // if ("checkout.session.completed".equals(event.getType())) {
    // EventDataObjectDeserializer deserializer = event.getDataObjectDeserializer();
    // StripeObject stripeObject = deserializer.getObject().orElse(null);

    // if (stripeObject instanceof Session) {
    // Session session = (Session) stripeObject;
    // String clientReferenceId = session.getClientReferenceId(); // Debe ser el ID
    // del usuario

    // if (clientReferenceId != null) {
    // // Buscar el usuario y actualizar su estado premium
    // UserEntity user = userRepository.findById(clientReferenceId).orElse(null);
    // if (user != null) {
    // // to-do: Implementar la lógica para verificar el estado de pago
    // // user.setPremium(true);
    // // userRepository.save(user);
    // }
    // }
    // }
    // }

    // return "Webhook processed successfully";
    // } catch (Exception e) {
    // e.printStackTrace();
    // throw new RuntimeException("Error processing webhook: " + e.getMessage(), e);
    // }
    // }
}

package com.arsansys.RemaPartners.services.stripe;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.arsansys.RemaPartners.controllers.dto.PaymentIntentDTO;
import com.arsansys.RemaPartners.repositories.UserRepository;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;

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
}

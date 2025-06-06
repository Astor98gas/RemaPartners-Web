package com.arsansys.RemaPartners.controllers.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.arsansys.RemaPartners.models.entities.RolEntity;
import com.arsansys.RemaPartners.models.entities.Suscripcion;
import com.arsansys.RemaPartners.models.entities.UserEntity;
import com.arsansys.RemaPartners.models.enums.ERol;
import com.arsansys.RemaPartners.services.UserService;
import com.arsansys.RemaPartners.services.stripe.SuscripcionService;
import com.stripe.Stripe;
import com.stripe.model.Customer;
import com.stripe.model.CustomerSearchResult;
import com.stripe.model.PromotionCode;
import com.stripe.model.PromotionCodeCollection;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controlador REST para la gestión de pagos y suscripciones con Stripe.
 * Permite crear sesiones de pago, activar pruebas gratuitas, verificar
 * historial de suscripciones y actualizar el estado de pago de los usuarios.
 */
@RestController
@RequestMapping("/api/stripe/")
@CrossOrigin(origins = "*") // Asegúrate de que esta línea esté presente
public class StripeController {

    @Autowired
    private SuscripcionService suscripcionService;

    @Autowired
    private UserService userService;

    @Value("${stripe.api.secret}")
    private String stripeApiKey;

    /**
     * Obtiene las suscripciones de un usuario por su ID.
     *
     * @param idUsuario ID del usuario.
     * @return Lista de suscripciones del usuario.
     */
    @GetMapping("/suscripciones/{idUsuario}")
    public ResponseEntity<?> getSuscripcionesByIdUsuario(@PathVariable String idUsuario) {
        try {
            return ResponseEntity.ok(suscripcionService.getSuscripcionesByIdUsuario(idUsuario));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    /**
     * Crea una sesión de pago de Stripe para el usuario.
     *
     * @param requestData Datos necesarios para crear la sesión (priceId,
     *                    successUrl, cancelUrl, clientReferenceId, promoCode).
     * @return URL de la sesión de pago creada o error si ocurre algún problema.
     */
    @PostMapping("/create-checkout-session")
    public ResponseEntity<Map<String, String>> createCheckoutSession(@RequestBody Map<String, Object> requestData) {
        try {
            Stripe.apiKey = stripeApiKey;

            // Obtener los datos del request
            String priceId = (String) requestData.get("priceId");
            String successUrl = (String) requestData.get("successUrl");
            String cancelUrl = (String) requestData.get("cancelUrl");
            String clientReferenceId = (String) requestData.get("clientReferenceId");
            String promoCode = (String) requestData.get("promoCode");

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

            // Obtener el usuario de la base de datos usando clientReferenceId
            UserEntity user = userService.getUserById(clientReferenceId);
            String userEmail = user.getEmail();
            String userName = user.getUsername();

            // Crear o recuperar el cliente en Stripe
            Customer customer = null;

            try {
                // Intentar encontrar cliente existente por email
                Map<String, Object> params = new HashMap<>();
                params.put("limit", 1);
                params.put("query", "email:'" + userEmail.replace("'", "\\'") + "'");

                CustomerSearchResult searchResult = Customer.search(params);

                if (searchResult.getData() != null && !searchResult.getData().isEmpty()) {
                    // Cliente existente encontrado
                    customer = searchResult.getData().get(0);

                    // Actualizar metadatos del cliente
                    Map<String, Object> metadata = new HashMap<>();
                    metadata.put("userId", clientReferenceId);
                    metadata.put("username", userName);

                    Map<String, Object> updateParams = new HashMap<>();
                    updateParams.put("metadata", metadata);
                    updateParams.put("name", userName); // Actualizar también el nombre

                    customer = customer.update(updateParams);
                } else {
                    // Cliente no encontrado, crear uno nuevo
                    Map<String, Object> customerParams = new HashMap<>();
                    customerParams.put("email", userEmail);
                    customerParams.put("name", userName);

                    Map<String, String> metadata = new HashMap<>();
                    metadata.put("userId", clientReferenceId);
                    metadata.put("username", userName);

                    customerParams.put("metadata", metadata);
                    customer = Customer.create(customerParams);
                }
            } catch (Exception e) {
                // Si hay un error en la búsqueda, crear un nuevo cliente
                Map<String, Object> customerParams = new HashMap<>();
                customerParams.put("email", userEmail);
                customerParams.put("name", userName);

                Map<String, String> metadata = new HashMap<>();
                metadata.put("userId", clientReferenceId);
                metadata.put("username", userName);

                customerParams.put("metadata", metadata);
                customer = Customer.create(customerParams);
            }

            // Crear la sesión de checkout asociada al cliente
            SessionCreateParams.Builder paramsBuilder = SessionCreateParams.builder()
                    // Configuración básica
                    .setMode(SessionCreateParams.Mode.PAYMENT)
                    .setClientReferenceId(clientReferenceId)

                    // URLs de redirección
                    .setSuccessUrl(successUrl)
                    .setCancelUrl(cancelUrl)

                    // Información del cliente
                    .setCustomer(customer.getId())

                    // Métodos de pago aceptados
                    .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)

                    // Dirección de facturación y envío
                    .setBillingAddressCollection(SessionCreateParams.BillingAddressCollection.REQUIRED)
                    .setShippingAddressCollection(
                            SessionCreateParams.ShippingAddressCollection.builder()
                                    .addAllowedCountry(SessionCreateParams.ShippingAddressCollection.AllowedCountry.ES)
                                    .build())

                    // Metadatos
                    .putMetadata("userId", clientReferenceId)
                    .putMetadata("username", userName)
                    .putMetadata("source", "REMA Partners Platform")
                    .putMetadata("email", userEmail)

                    // Línea de productos
                    .addLineItem(
                            SessionCreateParams.LineItem.builder()
                                    .setPrice(priceId)
                                    .setQuantity(1L)
                                    .build())

                    // Textos personalizados
                    .setCustomText(
                            SessionCreateParams.CustomText.builder()
                                    .setSubmit(
                                            SessionCreateParams.CustomText.Submit.builder()
                                                    .setMessage(
                                                            "Al pagar, aceptas nuestros términos y política de privacidad")
                                                    .build())
                                    .build());

            // Aplicar código promocional automáticamente si se proporciona
            boolean promoApplied = false;
            if (promoCode != null && !promoCode.isEmpty()) {
                try {
                    // Buscar el código promocional
                    Map<String, Object> promoParams = new HashMap<>();
                    promoParams.put("code", promoCode);

                    PromotionCodeCollection promotionCodes = PromotionCode.list(promoParams);

                    if (promotionCodes.getData().size() > 0) {
                        PromotionCode promotionCode = promotionCodes.getData().get(0);

                        // Añadir descuento a la sesión
                        paramsBuilder.addDiscount(
                                SessionCreateParams.Discount.builder()
                                        .setPromotionCode(promotionCode.getId())
                                        .build());

                        // Registrar la aplicación del código en los metadatos
                        paramsBuilder.putMetadata("promo_code_applied", promoCode);
                        promoApplied = true;
                    }
                } catch (Exception e) {
                    // Si ocurre un error al aplicar el código promocional, imprimir el error
                    e.printStackTrace();
                }
            }

            // Si no se pudo aplicar el código o no se proporcionó ninguno, permitir códigos
            // manuales
            if (!promoApplied) {
                paramsBuilder.setAllowPromotionCodes(true);
            }

            // Construir la sesión final
            SessionCreateParams params = paramsBuilder.build();
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

    /**
     * Activa una prueba gratuita para el usuario si no ha tenido suscripciones
     * previas.
     *
     * @param requestData Datos con el ID del usuario.
     * @return Estado de la activación de la prueba gratuita y la suscripción
     *         creada.
     */
    @PostMapping("/create-free-trial")
    public ResponseEntity<Map<String, Object>> createFreeTrial(@RequestBody Map<String, Object> requestData) {
        try {
            String userId = (String) requestData.get("userId");

            if (userId == null || userId.isEmpty()) {
                Map<String, Object> error = new HashMap<>();
                error.put("success", false);
                error.put("message", "User ID is required");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
            }

            // Obtener el usuario
            UserEntity user = userService.getUserById(userId);

            if (user == null) {
                Map<String, Object> error = new HashMap<>();
                error.put("success", false);
                error.put("message", "User not found");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
            }

            // Verificar si ya ha tenido alguna suscripción (activa o no)
            List<Suscripcion> todasLasSuscripciones = suscripcionService.getSuscripcionesByIdUsuario(userId);

            if (!todasLasSuscripciones.isEmpty()) {
                Map<String, Object> error = new HashMap<>();
                error.put("success", false);
                error.put("message", "Ya has utilizado tu prueba gratuita. Por favor, elige la suscripción premium.");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
            }

            // Crear nueva suscripción
            Suscripcion suscripcion = suscripcionService.createSuscripcion(userId);

            // Actualizar rol del usuario a VENDEDOR
            RolEntity rolVendedor = new RolEntity();
            rolVendedor.setName(ERol.VENDEDOR);
            user.setRol(rolVendedor);
            userService.updateUser(user);

            // Preparar respuesta
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Free trial activated successfully");
            response.put("subscription", suscripcion);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();

            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    /**
     * Verifica si el usuario ha tenido alguna suscripción anteriormente.
     *
     * @param userId ID del usuario.
     * @return Indica si el usuario ha tenido suscripciones previas.
     */
    @GetMapping("/check-subscription-history/{userId}")
    public ResponseEntity<Map<String, Object>> checkSubscriptionHistory(@PathVariable String userId) {
        try {
            // Verificar si el usuario existe
            UserEntity user = userService.getUserById(userId);

            if (user == null) {
                Map<String, Object> error = new HashMap<>();
                error.put("success", false);
                error.put("message", "User not found");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
            }

            // Verificar si ha tenido alguna suscripción
            List<Suscripcion> todasLasSuscripciones = suscripcionService.getSuscripcionesByIdUsuario(userId);
            boolean hasHadSubscription = !todasLasSuscripciones.isEmpty();

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("hasHadSubscription", hasHadSubscription);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();

            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    /**
     * Actualiza el estado de pago del usuario y activa la suscripción premium si
     * corresponde.
     *
     * @param requestData Datos con el ID del usuario y el estado de pago.
     * @return Estado de la operación y la suscripción creada si aplica.
     */
    @PostMapping("/update-user-payment")
    public ResponseEntity<Map<String, Object>> updateUserPayment(@RequestBody Map<String, Object> requestData) {
        try {
            String userId = (String) requestData.get("userId");
            Boolean hasPaid = (Boolean) requestData.get("hasPaid");

            if (userId == null || hasPaid == null) {
                Map<String, Object> error = new HashMap<>();
                error.put("success", false);
                error.put("message", "User ID and payment status are required");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
            }

            if (hasPaid) {
                // Obtener el usuario
                UserEntity user = userService.getUserById(userId);

                if (user == null) {
                    Map<String, Object> error = new HashMap<>();
                    error.put("success", false);
                    error.put("message", "User not found");
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
                }

                // Crear nueva suscripción
                Suscripcion suscripcion = suscripcionService.createSuscripcion(userId);

                // Actualizar rol del usuario a VENDEDOR
                RolEntity rolVendedor = new RolEntity();
                rolVendedor.setName(ERol.VENDEDOR);
                user.setRol(rolVendedor);
                userService.updateUser(user);

                // Preparar respuesta
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("message", "Premium subscription activated successfully");
                response.put("subscription", suscripcion);

                return ResponseEntity.ok(response);
            }

            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "No action taken");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();

            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
}

<template>
    <div class="w-full">
        <button @click="redirectToStripe"
            class="w-full px-4 py-3 mt-4 bg-blue-600 text-white font-medium rounded-lg hover:bg-blue-700 transition-colors flex items-center justify-center"
            :disabled="loading">
            <svg v-if="loading" class="animate-spin -ml-1 mr-2 h-5 w-5 text-white" xmlns="http://www.w3.org/2000/svg"
                fill="none" viewBox="0 0 24 24">
                <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"></circle>
                <path class="opacity-75" fill="currentColor"
                    d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z">
                </path>
            </svg>
            <span>{{ loading ? t('profile.processing') : t('profile.subscribe') }}</span>
        </button>
    </div>
</template>

<script>
import { useutf8Store } from '@/stores/counter';
import { useUsers } from '@/composables/useUsers';
import { ref, computed, onMounted } from 'vue';
import axios from 'axios';

// Configura la URL base de axios
axios.defaults.baseURL = 'http://localhost:8080/';

export default {
    emits: ['success', 'cancel', 'error'],
    setup() {
        const usersComposable = useUsers();
        const loading = ref(false);
        const baseUrl = window.location.origin;

        // Computed property para obtener el ID del usuario de forma segura
        const clientReferenceId = computed(() => {
            const currentUser = usersComposable.currentUser.value;
            return currentUser && currentUser.id ? currentUser.id : '';
        });

        const successURL = `${baseUrl}/profile?payment=success`;
        const cancelURL = `${baseUrl}/profile?payment=cancelled`;

        return {
            loading,
            clientReferenceId,
            successURL,
            cancelURL
        };
    },
    methods: {
        t(key) {
            const store = useutf8Store();
            return store.t(key) || key;
        },
        async redirectToStripe() {
            try {
                this.loading = true;

                // Datos a enviar
                const payload = {
                    priceId: 'price_1RMOTuQ99mt2tfSDk6OwTM1L',
                    clientReferenceId: this.clientReferenceId || 'default_user',
                    successUrl: this.successURL,
                    cancelUrl: this.cancelURL
                };

                // Configurar los headers explícitamente
                const config = {
                    headers: {
                        'Content-Type': 'application/json',
                        'Accept': 'application/json'
                    }
                };

                // Ahora la URL es relativa a la baseURL configurada
                const response = await axios.post(
                    '/api/stripe/create-checkout-session',
                    payload,
                    config
                );

                // Redirigir al checkout de Stripe usando la URL recibida
                if (response.data && response.data.url) {
                    window.location.href = response.data.url;
                } else {
                    throw new Error(this.t('profile.subscription_url_error'));
                }
            } catch (err) {
                this.loading = false;
                if (err.response && err.response.data && err.response.data.error) {
                    // Mostrar error específico del servidor si existe
                    this.$emit('error', err.response.data.error);
                } else {
                    // Mostrar error genérico si no hay respuesta específica
                    this.$emit('error', this.t('profile.subscription_error'));
                }
            }
        },
        handlePaymentResult() {
            // Verificar si hay un parámetro de éxito o cancelación en la URL
            const urlParams = new URLSearchParams(window.location.search);
            const status = urlParams.get('payment');

            if (status === 'success') {
                this.$emit('success');
                // Limpiar la URL de parámetros
                if (window.history.replaceState) {
                    const newUrl = window.location.href.split('?')[0];
                    window.history.replaceState({ path: newUrl }, '', newUrl);
                }
            } else if (status === 'cancelled') {
                this.$emit('cancel');
                // Limpiar la URL de parámetros
                if (window.history.replaceState) {
                    const newUrl = window.location.href.split('?')[0];
                    window.history.replaceState({ path: newUrl }, '', newUrl);
                }
            }
        }
    },
    mounted() {
        // Verificar si hay parámetros de redirección al cargar el componente
        this.handlePaymentResult();
    }
};
</script>
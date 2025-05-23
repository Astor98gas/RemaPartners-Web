<template>
    <div class="w-full">
        <!-- Campo de código promocional -->
        <div class="mb-4" v-if="showPromoInput">
            <label class="block text-gray-700 font-medium mb-2">{{ t('profile.promo_code') }}</label>
            <div class="flex">
                <input type="text" v-model="promoCode"
                    class="flex-1 px-4 py-2 border rounded-l-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
                    :placeholder="t('profile.promo_code_placeholder')" />
                <button @click="verifyPromoCode"
                    class="px-4 py-2 bg-blue-600 text-white rounded-r-lg hover:bg-blue-700 transition-colors">
                    {{ t('common.apply') }}
                </button>
            </div>
            <p v-if="promoCodeValid" class="mt-2 text-green-600">
                {{ t('profile.promo_code_valid') }}
            </p>
            <p v-if="promoCodeError" class="mt-2 text-red-600">
                {{ promoCodeError }}
            </p>
        </div>

        <p class="text-blue-600 hover:underline cursor-pointer mb-4" @click="showPromoInput = !showPromoInput">
            {{ showPromoInput ? t('profile.hide_promo') : t('profile.have_promo') }}
        </p>

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
        const showPromoInput = ref(false);
        const promoCode = ref('');
        const promoCodeValid = ref(false);
        const promoCodeError = ref('');

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
            cancelURL,
            showPromoInput,
            promoCode,
            promoCodeValid,
            promoCodeError
        };
    },
    methods: {
        /**
         * Traduce una clave utilizando el store de internacionalización.
         * @param {string} key - Clave de traducción.
         * @returns {string} Traducción correspondiente o la clave si no existe.
         */
        t(key) {
            const store = useutf8Store();
            return store.t(key) || key;
        },

        /**
         * Verifica el código promocional ingresado por el usuario.
         * Si el campo está vacío, muestra un error. Si no, lo marca como válido.
         * Este método puede ser extendido para realizar una verificación real vía API.
         * @async
         */
        async verifyPromoCode() {
            if (!this.promoCode.trim()) {
                this.promoCodeError = this.t('profile.promo_code_empty');
                return;
            }

            try {
                // Aquí podrías hacer una verificación previa si implementas este endpoint
                // const response = await axios.get(`/api/stripe/verify-promo/${this.promoCode}`);
                // if (response.data.valid) {
                //     this.promoCodeValid = true;
                //     this.promoCodeError = '';
                // } else {
                //     this.promoCodeError = response.data.message || this.t('profile.promo_code_invalid');
                //     this.promoCodeValid = false;
                // }

                // Simplificado: solo validamos que no esté vacío
                this.promoCodeValid = true;
                this.promoCodeError = '';
            } catch (err) {
                this.promoCodeError = this.t('profile.promo_code_error');
                this.promoCodeValid = false;
            }
        },

        /**
         * Redirige al usuario al checkout de Stripe.
         * Envía los datos necesarios al backend para crear una sesión de pago.
         * Incluye el código promocional si es válido.
         * @async
         */
        async redirectToStripe() {
            try {
                this.loading = true;

                // Verificar el usuario en tiempo real antes de enviar la solicitud
                const usersComposable = useUsers();
                const userData = await usersComposable.isLoggedIn();
                const userId = userData && userData.id ? userData.id : '';

                // Datos a enviar
                const payload = {
                    priceId: 'price_1RMOTuQ99mt2tfSDk6OwTM1L',
                    clientReferenceId: userId || this.clientReferenceId || 'default_user',
                    successUrl: this.successURL,
                    cancelUrl: this.cancelURL
                };

                // Añadir código promocional si está presente y validado
                if (this.promoCodeValid && this.promoCode.trim()) {
                    payload.promoCode = this.promoCode.trim();
                }

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

        /**
         * Maneja el resultado del pago leyendo los parámetros de la URL.
         * Emite eventos según el resultado y limpia los parámetros de la URL.
         */
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
<template>
    <div class="fixed inset-0 bg-black/60 flex items-center justify-center z-50 p-4 overflow-y-auto">
        <div class="bg-white rounded-xl shadow-2xl w-full max-w-lg p-6 md:p-8 animate-fadeIn my-8 relative">
            <div class="flex justify-between items-center mb-5">
                <h3 class="text-xl md:text-2xl font-bold text-gray-900">{{ title }}</h3>
                <button @click="$emit('close')" class="text-gray-500 hover:text-gray-700 transition-colors">
                    <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" fill="none" viewBox="0 0 24 24"
                        stroke="currentColor">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                            d="M6 18L18 6M6 6l12 12" />
                    </svg>
                </button>
            </div>

            <!-- Product info -->
            <div class="flex items-center mb-6 p-4 bg-blue-50 rounded-lg border border-blue-100 shadow-sm">
                <div class="w-20 h-20 flex-shrink-0 rounded-md overflow-hidden mr-4">
                    <img :src="productImage" :alt="productTitle" class="w-full h-full object-cover"
                        @error="onImageError">
                </div>
                <div class="flex-1 min-w-0">
                    <h4 class="font-medium text-gray-900 text-base mb-1 truncate" :title="productTitle">{{ productTitle
                    }}</h4>
                    <p class="text-gray-700 text-sm font-semibold bg-white inline-block px-2 py-1 rounded-md">
                        {{ formatCurrency(originalPrice, currency) }}
                    </p>
                </div>
            </div>

            <!-- Input form -->
            <form @submit.prevent="submitOffer" class="space-y-5">
                <div>
                    <label for="offerAmount" class="block text-sm font-medium text-gray-700 mb-2">
                        {{ t('producto.makeOffer') }}
                    </label>
                    <div class="relative rounded-md shadow-sm">
                        <div class="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
                            <span class="text-gray-500 font-medium">{{ currency }}</span>
                        </div>
                        <input type="number" :step="0.01" min="0.01" v-model="offerAmount" id="offerAmount"
                            class="focus:ring-blue-500 focus:border-blue-500 block w-full pl-14 pr-5 py-3 border-gray-300 rounded-lg shadow-sm text-lg font-medium"
                            :placeholder="formatNumber(originalPrice / 100)" required />
                    </div>
                </div>

                <div class="flex justify-end space-x-3 pt-2">
                    <button type="button" @click="$emit('close')"
                        class="px-5 py-2.5 bg-gray-200 text-gray-800 font-medium rounded-lg hover:bg-gray-300 transition-colors focus:outline-none focus:ring-2 focus:ring-gray-300 focus:ring-offset-2">
                        {{ t('common.cancel') }}
                    </button>
                    <button type="submit"
                        class="px-5 py-2.5 bg-blue-600 text-white font-medium rounded-lg hover:bg-blue-700 transition-colors focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-offset-2 flex items-center"
                        :class="{ 'opacity-70 cursor-not-allowed': !isValidOffer, 'hover:bg-blue-700': isValidOffer }"
                        :disabled="!isValidOffer">
                        <svg v-if="isValidOffer" xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 mr-1.5" fill="none"
                            viewBox="0 0 24 24" stroke="currentColor">
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                d="M9 12l2 2 4-4m6 2a9 9 9 9 11-18 0 9 9 9 11-18 0z" />
                        </svg>
                        {{ t('producto.sendOffer') }}
                    </button>
                </div>
            </form>
        </div>
    </div>
</template>

<script>
import { useutf8Store } from '@/stores/counter';

export default {
    name: 'OfferModal',
    props: {
        productTitle: {
            type: String,
            required: true
        },
        productImage: {
            type: String,
            required: true
        },
        originalPrice: {
            type: Number,
            required: true
        },
        currency: {
            type: String,
            default: '€'
        }
    },
    data() {
        return {
            offerAmount: null,
            title: this.t('producto.offerTitle'),
            placeholderImg: new URL('@/assets/logoCuadrado.jpeg', import.meta.url).href
        };
    },
    computed: {
        isValidOffer() {
            const amount = parseFloat(this.offerAmount);
            return amount && amount > 0;
        }
    },
    mounted() {
        // Initialize with original price
        this.offerAmount = this.formatNumber(this.originalPrice / 100);

        // Add event listener to close modal on escape key
        document.addEventListener('keydown', this.handleEscapeKey);
    },
    beforeUnmount() {
        // Remove event listener when component is destroyed
        document.removeEventListener('keydown', this.handleEscapeKey);
    },
    methods: {
        t(key) {
            const store = useutf8Store();
            return store.t(key);
        },
        formatCurrency(price, currency) {
            return `${currency} ${this.formatNumber(price / 100)}`;
        },
        formatNumber(value) {
            return typeof value === 'number' ? value.toFixed(2) : value;
        },
        submitOffer() {
            if (!this.isValidOffer) return;

            const offerData = {
                amount: parseFloat(this.offerAmount),
                currency: this.currency,
                formattedOffer: `${this.currency} ${this.formatNumber(parseFloat(this.offerAmount))}`
            };

            this.$emit('offer-submitted', offerData);
        },
        onImageError(event) {
            event.target.src = this.placeholderImg;
        },
        handleEscapeKey(event) {
            if (event.key === 'Escape') {
                this.$emit('close');
            }
        }
    }
};
</script>

<style scoped>
.animate-fadeIn {
    animation: fadeIn 0.4s cubic-bezier(0.16, 1, 0.3, 1);
}

@keyframes fadeIn {
    from {
        opacity: 0;
        transform: translateY(-10px) scale(0.98);
    }

    to {
        opacity: 1;
        transform: translateY(0) scale(1);
    }
}

/* Fix for the number input spinner */
input[type="number"]::-webkit-inner-spin-button,
input[type="number"]::-webkit-outer-spin-button {
    opacity: 0.3;
    height: 24px;
}
</style>
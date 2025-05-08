<template>
    <div>
        <stripe-checkout ref="checkoutRef" mode="payment" :pk="publishableKey" :line-items="lineItems"
            :success-url="successURL" :cancel-url="cancelURL" @loading="v => loading = v" />
        <button @click="submit">Pay now!</button>
    </div>
</template>

<script>
import { StripeCheckout } from '@vue-stripe/vue-stripe';
export default {
    components: {
        StripeCheckout,
    },
    data() {
        this.publishableKey = '';
        return {
            loading: false,
            lineItems: [
                {
                    price: '', // The id of the one-time price you created in your Stripe dashboard
                    quantity: 1,
                },
            ],
            successURL: 'http://localhost:5173/',
            cancelURL: 'http://localhost:5173/profile',
        };
    },
    methods: {
        submit() {
            // You will be redirected to Stripe's secure checkout page
            this.$refs.checkoutRef.redirectToCheckout();
        },
    },
};
</script>
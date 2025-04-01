<template>
    <div class="my-component">
        <h1>{{ message }}</h1>
        <input v-model="message" placeholder="Type a new message" />
        <button @click="updateMessage">Change Message</button>
    </div>
</template>

<script>
import { useMessageStore } from '@/stores/counter';

export default {
    data() {
        return {
            message: 'Hello from MyComponent!'
        };
    },
    methods: {
        updateMessage() {
            // Importante: usamos el store importado directamente
            const store = useMessageStore();
            this.message = store.message;
        }
    },
    mounted() {
    }
};
</script>
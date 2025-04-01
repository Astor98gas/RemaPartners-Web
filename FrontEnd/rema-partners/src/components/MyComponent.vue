<template>
    <div class="my-component">
        <h1>{{ message }}</h1>
        <input v-model="formData.username" placeholder="Username" />
        <input v-model="formData.email" placeholder="Email" />
        <input v-model="formData.password" type="password" placeholder="Password" />
        <button @click="insertUser">Submit</button>
        <p v-if="error" class="error">{{ error }}</p>
        <p v-if="success" class="success">{{ success }}</p>
    </div>
</template>

<script>
import { useMessageStore } from '@/stores/counter';
import axios from 'axios';

export default {
    data() {
        return {
            message: 'Hello from MyComponent!',
            formData: {
                username: '',
                email: '',
                password: ''
            },
            error: null,
            success: null
        };
    },
    methods: {
        updateMessage() {
            const store = useMessageStore();
            this.message = store.message;
        },
        async insertUser() {
            try {
                const response = await axios.post('http://localhost:8080/createUser', this.formData);
                this.success = 'User created successfully!';
                this.error = null;
                this.formData = {
                    username: '',
                    email: '',
                    password: ''
                };
            } catch (error) {
                this.error = error.response?.data?.message || 'An error occurred';
                this.success = null;
            }
        }
    },
    mounted() {
    }
};
</script>

<style scoped>
.my-component {
    max-width: 400px;
    margin: 0 auto;
    padding: 20px;
}

input {
    display: block;
    width: 100%;
    margin-bottom: 10px;
    padding: 8px;
}

button {
    background-color: #4CAF50;
    color: white;
    padding: 10px 15px;
    border: none;
    border-radius: 4px;
    cursor: pointer;
}

button:hover {
    background-color: #45a049;
}

.error {
    color: red;
}

.success {
    color: green;
}
</style>
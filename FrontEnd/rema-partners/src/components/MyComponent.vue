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
    <div v-for="user in users" :key="user.id">
        <ul>
            <input v-model="user.id" type="hidden">
            <input v-model="user.username" type="text">
            <input v-model="user.email" type="text">
            <input v-model="user.password" type="password">
            <button @click="updateUser(user)">Update</button>
            <button @click="deleteUser(user.username)">Delete</button>
        </ul>
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
            success: null,
            users: []
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
                await this.getUsers(); // Refresh the users list
            } catch (error) {
                this.error = error.response?.data?.message || 'An error occurred';
                this.success = null;
            }
        },
        async getUsers() {
            try {
                const response = await axios.get('http://localhost:8080/getUsers');
                this.users = response.data;
            } catch (error) {
                this.error = error.response?.data?.message || 'Error fetching users';
                console.error('Error fetching users:', error);
            }
        },
        async deleteUser(username) {
            try {
                await axios.get(`http://localhost:8080/deleteUser/${username}`);
                this.success = 'User deleted successfully!';
                this.error = null;
                await this.getUsers(); // Refresh the list after deletion
            } catch (error) {
                this.error = error.response?.data || 'Error deleting user';
                this.success = null;
            }
        },
        async updateUser(user) {
            try {
                const userData = {
                    username: user.username,
                    email: user.email,
                    password: user.password
                };

                await axios.post(`http://localhost:8080/updateUser/${user.id}`, userData);
                this.success = 'User updated successfully!';
                this.error = null;
                await this.getUsers(); // Refresh the list after update
            } catch (error) {
                this.error = error.response?.data || 'Error updating user';
                this.success = null;
            }
        }
    },
    mounted() {
        this.getUsers();
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
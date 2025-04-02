<template>
    <div class="min-h-screen bg-gradient-to-r from-blue-500 to-purple-600 p-8">
        <div class="max-w-4xl mx-auto bg-white rounded-lg shadow-xl p-6 mb-8">
            <h1 class="text-3xl font-bold mb-6 text-gray-800">{{ message }}</h1>

            <div class="space-y-4 mb-6">
                <input v-model="formData.username" placeholder="Username"
                    class="w-full px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500" />
                <input v-model="formData.email" placeholder="Email"
                    class="w-full px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500" />
                <input v-model="formData.password" type="password" placeholder="Password"
                    class="w-full px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500" />
                <button @click="insertUser"
                    class="w-full bg-blue-600 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded-lg transition duration-200">Submit</button>
            </div>

            <p v-if="error" class="bg-red-100 border-l-4 border-red-500 text-red-700 p-4 mb-4">{{ error }}</p>
            <p v-if="success" class="bg-green-100 border-l-4 border-green-500 text-green-700 p-4 mb-4">{{ success }}</p>
        </div>

        <div class="max-w-4xl mx-auto bg-white rounded-lg shadow-xl p-6">
            <h2 class="text-2xl font-bold mb-6 text-gray-800">User List</h2>
            <div v-for="user in users" :key="user.id" class="bg-gray-50 p-4 rounded-lg mb-4 shadow">
                <div class="grid grid-cols-1 md:grid-cols-4 gap-4">
                    <input v-model="user.id" type="hidden">
                    <input v-model="user.username" type="text"
                        class="px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500">
                    <input v-model="user.email" type="text"
                        class="px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500">
                    <input v-model="user.password" type="password"
                        class="px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500">

                    <div class="flex space-x-2">
                        <button @click="updateUser(user)"
                            class="bg-yellow-500 hover:bg-yellow-600 text-white font-bold py-2 px-4 rounded-lg transition duration-200">Update</button>
                        <button @click="deleteUser(user.username)"
                            class="bg-red-500 hover:bg-red-600 text-white font-bold py-2 px-4 rounded-lg transition duration-200">Delete</button>
                    </div>
                </div>
            </div>
        </div>
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
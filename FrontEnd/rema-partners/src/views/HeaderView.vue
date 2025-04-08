<template>
    <header class="sticky top-0 w-full bg-white shadow-md z-50">
        <div class="max-w-7xl mx-auto px-4 py-3 flex justify-between items-center">
            <router-link to="/">
                <div class="flex items-center">
                    <img src="@/assets/logo.png" alt="REMA Partners Logo" class="h-10 mr-3">
                    <h1 class="text-2xl font-bold text-gray-800">REMA Partners</h1>
                </div>
            </router-link>

            <nav class="hidden md:block">
                <ul class="flex space-x-2">
                    <li><router-link to="/home"
                            class="px-4 py-2 text-gray-700 font-medium hover:text-green-600 transition-colors">Home</router-link>
                    </li>

                </ul>
            </nav>

            <div class="hidden md:flex space-x-3">
                <button
                    class="px-4 py-2 border border-green-500 text-green-500 font-medium rounded hover:bg-green-50 transition-colors">Login</button>
                <button
                    class="px-4 py-2 bg-green-500 border border-green-500 text-white font-medium rounded hover:bg-green-600 transition-colors">Sign
                    Up</button>
            </div>

            <button class="md:hidden focus:outline-none" @click="toggleMobileMenu">
                <div class="w-6 h-0.5 bg-gray-700 relative transition-all duration-300"
                    :class="{ 'bg-transparent': mobileMenuOpen }">
                    <div class="absolute w-6 h-0.5 bg-gray-700 transition-all duration-300"
                        :class="mobileMenuOpen ? 'rotate-45 top-0' : '-translate-y-2'"></div>
                    <div class="absolute w-6 h-0.5 bg-gray-700 transition-all duration-300"
                        :class="mobileMenuOpen ? '-rotate-45 top-0' : 'translate-y-2'"></div>
                </div>
            </button>
        </div>

        <div class="md:hidden bg-white shadow-lg" :class="mobileMenuOpen ? 'block' : 'hidden'">
            <ul class="p-4 space-y-4">
                <li><router-link to="/home"
                        class="block px-4 py-2 text-gray-700 font-medium hover:text-green-600 transition-colors"
                        @click="closeMobileMenu">Home</router-link></li>
                <li class="pt-2 flex flex-col space-y-2">
                    <button
                        class="w-full px-4 py-2 border border-green-500 text-green-500 font-medium rounded hover:bg-green-50 transition-colors">Login</button>
                    <button
                        class="w-full px-4 py-2 bg-green-500 border border-green-500 text-white font-medium rounded hover:bg-green-600 transition-colors">Sign
                        Up</button>
                </li>
            </ul>
        </div>
    </header>
</template>

<script lang="ts">
import { defineComponent, ref } from 'vue';

export default defineComponent({
    name: 'HeaderView',
    setup() {
        const mobileMenuOpen = ref(false);

        const toggleMobileMenu = () => {
            mobileMenuOpen.value = !mobileMenuOpen.value;
        };

        const closeMobileMenu = () => {
            mobileMenuOpen.value = false;
        };

        return {
            mobileMenuOpen,
            toggleMobileMenu,
            closeMobileMenu,
        };
    }
});
</script>

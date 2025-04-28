<template>
    <header class="sticky top-0 w-full bg-gray-100 shadow-md z-50">
        <div class="max-w-7xl mx-auto px-4 py-3 flex justify-between items-center">
            <div @click="reload" class="flex items-center cursor-pointer">
                <div class="flex items-center">
                    <img src="@/assets/logo.png" alt="REMA Partners Logo" class="h-10 mr-3">
                    <h1 class="text-2xl font-bold text-gray-800">REMA Partners</h1>
                </div>
            </div>

            <nav class="hidden md:block">

            </nav>

            <div class="profile" v-if="isLoggedIn">
                <div class="hidden md:flex space-x-3">
                    <span class="text-gray-700 mt-2.5">{{ welcomeMessage }}</span>
                    <router-link to="/profile">
                        <img src="@/assets/icons/profile.png" alt="Profile" class="h-10 w-10 rounded-full">
                    </router-link>
                    <SelectorIdioma class="md:flex" />
                </div>
            </div>
            <div v-else>
                <div class="hidden md:flex space-x-3">
                    <router-link to="/login">
                        <ButtonBasic variant="secondary" size="sm" class="text-lg">
                            {{ utf8.t('login.login') }}
                        </ButtonBasic>
                    </router-link>
                    <router-link to="/signup">
                        <ButtonBasic variant="primary" size="base" class="text-base">
                            {{ utf8.t('login.register') }}
                        </ButtonBasic>
                    </router-link>
                    <SelectorIdioma class="md:flex" />
                </div>
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
                <li><router-link to="/"
                        class="block px-4 py-2 text-gray-700 font-medium hover:text-orange-600 transition-colors"
                        @click="closeMobileMenu">Home</router-link></li>
                <li class="pt-2 flex flex-col space-y-2">
                    <router-link to="/login" @click="closeMobileMenu">
                        <button
                            class="w-full px-4 py-2 border border-orange-500 text-orange-500 font-medium rounded hover:bg-orange-50 transition-colors">Login</button>
                    </router-link>
                    <router-link to="/signup" @click="closeMobileMenu">
                        <button
                            class="w-full px-4 py-2 bg-orange-500 border border-orange-500 text-white font-medium rounded hover:bg-orange-600 transition-colors">Sign
                            Up</button>
                    </router-link>
                </li>
                <SelectorIdioma />
            </ul>
        </div>
    </header>
</template>

<script>
import ButtonBasic from '@/components/ui/ButtonBasic.vue';
import SelectorIdioma from '@/components/ui/SelectorIdioma.vue';
import { useUsers } from '@/composables/useUsers';
import { useutf8Store } from '@/stores/counter';

export default {
    name: 'HeaderView',
    components: {
        SelectorIdioma,
        ButtonBasic
    },
    data() {
        return {
            mobileMenuOpen: false,
            utf8: useutf8Store(),
            usersComposable: useUsers(),
            currentUser: null
        };
    },
    computed: {
        isLoggedIn() {
            return !!this.currentUser;
        },
        welcomeMessage() {
            return this.currentUser ? `Hola, ${this.currentUser.username}` : '';
        }
    },
    watch: {
        'usersComposable.currentUser': {
            handler(newVal) {
                this.currentUser = newVal;
            },
            immediate: true
        }
    },
    mounted() {
        this.checkLoginStatus();
    },
    methods: {
        async checkLoginStatus() {
            try {
                await this.usersComposable.isLoggedIn();
            } catch (error) {
                console.error('Error checking login status:', error);
            }
        },
        toggleMobileMenu() {
            this.mobileMenuOpen = !this.mobileMenuOpen;
        },
        closeMobileMenu() {
            this.mobileMenuOpen = false;
        },
        reload() {
            window.location.href = '/';
        }
    }
}
</script>
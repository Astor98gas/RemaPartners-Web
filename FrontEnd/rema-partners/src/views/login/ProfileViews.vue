<template>
    <div class="min-h-screen flex items-center justify-center bg-gray-100 py-12 px-4 sm:px-6 lg:px-8">
        <div class="max-w-md w-full space-y-8 shadow-2xl rounded-3xl bg-white p-6">
            <ButtonBasic variant="primary" size="lg" @click="logout">
                <span v-if="currentUser != null">{{ welcomeMessage }}</span>
                <span v-else>Login</span>
            </ButtonBasic>
        </div>
    </div>
</template>

<script lang="ts">
import ButtonBasic from '@/components/ui/ButtonBasic.vue';
import { useUsers } from '@/composables/useUsers';
import { useutf8Store } from '@/stores/counter';


export default {
    name: 'ProfileViews',
    components: {
        ButtonBasic,
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
        logout() {
            this.usersComposable.logout()
                .then(() => {
                    console.log('Logout successful');
                    this.$router.push({ name: 'login' });
                })
                .catch((error) => {
                    console.error('Logout error:', error);
                });
        }
    }
};


</script>
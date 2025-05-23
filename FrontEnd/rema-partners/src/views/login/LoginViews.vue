<template>
    <div class="min-h-screen flex items-center justify-center bg-gray-100 py-12 px-4 sm:px-6 lg:px-8">
        <div class="max-w-md w-full space-y-8 shadow-2xl rounded-3xl bg-white p-6">
            <div class="flex justify-center mb-4">
                <img src="@/assets/logo.png" alt="REMA Partners Logo" class="h-16">
            </div>
            <div>
                <h2 class="mt-6 text-center text-3xl font-extrabold text-gray-900">
                    {{ utf8.t('login.title') }}
                </h2>
            </div>
            <form class="mt-8 space-y-6" @submit.prevent="handleLogin">
                <div class="space-y-4">
                    <InputText v-model="userLogin.username" type="text" :placeholder="utf8.t('login.username')"
                        :label="utf8.t('login.username')" required />
                    <InputText v-model="userLogin.password" type="password" :placeholder="utf8.t('login.password')"
                        :label="utf8.t('login.password')" required />
                </div>

                <div class="flex items-center justify-between">
                    <div class="text-sm">
                        <a href="#" class="font-medium text-indigo-600 hover:text-indigo-500">
                            {{ utf8.t('login.forgot') }}
                        </a>
                    </div>
                </div>

                <div>
                    <button type="submit"
                        class="group relative w-full flex justify-center py-2 px-4 border border-transparent text-sm font-medium rounded-md text-white bg-indigo-600 hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500">
                        {{ utf8.t('login.submit') }}
                    </button>
                </div>
            </form>
        </div>

        <!-- Modal para mostrar errores -->
        <Modal v-model="showErrorModal" :title="utf8.t('modal.error')">
            <p>{{ error }}</p>
        </Modal>
    </div>
</template>

<script lang="ts">
/**
 * Vista de inicio de sesión.
 * Permite a los usuarios autenticarse en la plataforma mediante nombre de usuario y contraseña.
 * Maneja la validación de entradas y muestra mensajes de error cuando corresponde.
 */
import InputText from '@/components/ui/InputText.vue'
import Modal from '@/components/ui/Modal.vue'
import { useutf8Store } from '@/stores/counter'
import { useUsers } from '@/composables/useUsers'
import type { UserLogin } from '@/models/user'
import { useRoute } from 'vue-router';
import { useToast } from 'vue-toastification';

export default {
    name: 'LoginView',
    components: {
        InputText,
        Modal
    },
    data() {
        return {
            userLogin: {
                username: '',
                password: '',
            } as UserLogin,
            rememberMe: false,
            utf8: useutf8Store(),
            error: null as string | null,
            showErrorModal: false,
            toast: useToast(),
            route: useRoute(),
        }
    },
    methods: {
        /**
         * Maneja el intento de inicio de sesión.
         * Valida las entradas del usuario y realiza la petición de autenticación.
         * Muestra errores si las credenciales son inválidas o si faltan campos.
         */
        handleLogin() {
            this.error = null
            if (!this.userLogin.username || !this.userLogin.password) {
                this.error = this.utf8.t('login.allInputs')
                this.showErrorModal = true // Mostrar modal
                return
            }
            useUsers().loginUser(this.userLogin)
                .then((response) => {
                    // Comentado: console.log de respuesta exitosa de login
                    // console.log('Login successful:', response)
                    this.$router.push({ name: 'root' })
                })
                .catch((error) => {
                    console.error('Login error:', error.response?.data || error.message || error)
                    this.error = this.utf8.t('login.usernamePassword')
                    this.showErrorModal = true // Mostrar modal
                })
        }
    },
    /**
     * Ciclo de vida que se ejecuta al montar el componente.
     * Verifica si hay errores de sesión caducada y muestra notificaciones correspondientes.
     */
    mounted() {
        if (this.route.query.error === 'session_expired') {
            this.toast.error(this.utf8.t('login.session_expired'));
        }
    }
}
</script>
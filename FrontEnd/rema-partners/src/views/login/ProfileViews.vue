<template>
    <div class="min-h-screen bg-gray-100 py-8 px-4 sm:px-6 lg:px-8">
        <div class="max-w-4xl mx-auto">
            <!-- Sección de carga -->
            <div v-if="loading" class="flex justify-center items-center h-64">
                <div class="animate-spin rounded-full h-12 w-12 border-t-2 border-b-2 border-blue-500"></div>
            </div>

            <!-- Sección de error -->
            <div v-else-if="error" class="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded-lg mb-6">
                <p>{{ error }}</p>
                <ButtonBasic variant="primary" size="sm" class="mt-2" @click="$router.push({ name: 'login' })">
                    {{ utf8.t('profile.back_to_login') }}
                </ButtonBasic>
            </div>

            <!-- Perfil de usuario -->
            <div v-else-if="currentUser" class="bg-white shadow overflow-hidden rounded-lg">
                <!-- Cabecera -->
                <div class="bg-gradient-to-r from-blue-500 to-indigo-600 px-6 py-5">
                    <h1 class="text-2xl font-semibold text-white">{{ utf8.t('profile.title') }}</h1>
                </div>

                <!-- Información del perfil -->
                <div class="grid md:grid-cols-2 gap-6 p-6">
                    <div class="col-span-2 md:col-span-1">
                        <div class="rounded-lg bg-gray-50 p-6 shadow-md">
                            <h2 class="text-xl font-semibold border-b pb-2 mb-4">{{ utf8.t('profile.account_info') }}
                            </h2>

                            <div v-if="!editMode" class="space-y-4">
                                <div>
                                    <p class="text-sm text-gray-500">{{ utf8.t('profile.username') }}</p>
                                    <p class="text-lg font-medium">{{ currentUser.username }}</p>
                                </div>
                                <div>
                                    <p class="text-sm text-gray-500">{{ utf8.t('profile.email') }}</p>
                                    <p class="text-lg font-medium">{{ currentUser.email }}</p>
                                </div>
                                <div>
                                    <p class="text-sm text-gray-500">{{ utf8.t('profile.role') }}</p>
                                    <p class="text-lg font-medium">{{ currentUser.rol?.name || 'N/A' }}</p>
                                </div>
                                <ButtonBasic variant="primary" size="md" @click="toggleEditMode">
                                    {{ utf8.t('profile.edit_profile') }}
                                </ButtonBasic>
                            </div>

                            <form v-else @submit.prevent="updateProfile" class="space-y-4">
                                <div>
                                    <label class="block text-sm text-gray-500">{{ utf8.t('profile.username') }}</label>
                                    <input type="text" v-model="formData.username"
                                        class="w-full px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
                                        required>
                                </div>
                                <div>
                                    <label class="block text-sm text-gray-500">{{ utf8.t('profile.email') }}</label>
                                    <input type="email" v-model="formData.email"
                                        class="w-full px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
                                        required>
                                </div>
                                <div>
                                    <label class="block text-sm text-gray-500">{{ utf8.t('profile.new_password')
                                    }}</label>
                                    <input type="password" v-model="formData.password"
                                        class="w-full px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500">
                                    <p class="text-xs text-gray-500 mt-1">{{ utf8.t('profile.password_note') }}</p>
                                </div>
                                <div>
                                    <label class="block text-sm text-gray-500">{{ utf8.t('profile.confirm_password')
                                    }}</label>
                                    <input type="password" v-model="formData.confirmPassword"
                                        class="w-full px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500">
                                </div>
                                <div class="flex space-x-3">
                                    <ButtonBasic variant="primary" size="md" type="submit" :disabled="updating">
                                        {{ utf8.t('profile.save') }}
                                    </ButtonBasic>
                                    <ButtonBasic variant="secondary" size="md" @click="cancelEdit">
                                        {{ utf8.t('profile.cancel') }}
                                    </ButtonBasic>
                                </div>
                            </form>
                        </div>
                    </div>

                    <div class="col-span-2 md:col-span-1">
                        <div class="rounded-lg bg-gray-50 p-6 shadow-md">
                            <h2 class="text-xl font-semibold border-b pb-2 mb-4">{{ utf8.t('profile.account_actions') }}
                            </h2>
                            <div class="space-y-4">
                                <!-- Enlaces basados en el rol -->
                                <div v-if="isVendedor || isAdmin" class="border-b pb-4">
                                    <h3 class="font-medium mb-2">{{ utf8.t('profile.your_products') }}</h3>
                                    <router-link to="/producto/create"
                                        class="text-blue-600 hover:text-blue-800 block py-1">
                                        {{ utf8.t('profile.add_product') }}
                                    </router-link>
                                    <!-- Aquí podrías agregar un enlace a "Mis productos" en el futuro -->
                                </div>

                                <div v-if="isAdmin" class="border-b pb-4">
                                    <h3 class="font-medium mb-2">{{ utf8.t('profile.admin_actions') }}</h3>
                                    <router-link to="/admin/categoria/list"
                                        class="text-blue-600 hover:text-blue-800 block py-1">
                                        {{ utf8.t('profile.manage_categories') }}
                                    </router-link>
                                    <router-link to="/admin/categoria/create"
                                        class="text-blue-600 hover:text-blue-800 block py-1">
                                        {{ utf8.t('profile.create_category') }}
                                    </router-link>
                                </div>

                                <div v-if="isComprador || isVendedor || isAdmin" class="border-b pb-4">
                                    <h3 class="font-medium mb-2">{{ utf8.t('profile.communication') }}</h3>
                                    <router-link to="/chats" class="text-blue-600 hover:text-blue-800 block py-1">
                                        {{ utf8.t('profile.view_chats') }}
                                    </router-link>
                                </div>

                                <!-- New Subscription Section -->
                                <div class="border-b pb-4">
                                    <h3 class="font-medium mb-2">{{ utf8.t('profile.subscription') || 'Subscription' }}
                                    </h3>
                                    <ButtonBasic variant="success" size="md" @click="showStripeModal = true"
                                        class="flex items-center">
                                        <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 mr-2" fill="none"
                                            viewBox="0 0 24 24" stroke="currentColor">
                                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                                d="M17 9V7a2 2 0 00-2-2H5a2 2 0 00-2 2v6a2 2 0 002 2h2m2 4h10a2 2 0 002-2v-6a2 2 0 00-2-2H9a2 2 0 00-2 2v6a2 2 0 002 2z" />
                                        </svg>
                                        {{ utf8.t('profile.subscribe') || 'Subscribe Premium' }}
                                    </ButtonBasic>
                                </div>

                                <div class="pt-2">
                                    <ButtonBasic variant="danger" size="md" @click="logout">
                                        {{ utf8.t('profile.logout') }}
                                    </ButtonBasic>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Usuario no autenticado -->
            <div v-else class="bg-white shadow overflow-hidden rounded-lg p-6 text-center">
                <h2 class="text-xl font-semibold mb-4">{{ utf8.t('profile.not_logged_in') }}</h2>
                <p class="mb-4">{{ utf8.t('profile.login_required_message') }}</p>
                <div class="flex justify-center space-x-4">
                    <ButtonBasic variant="primary" size="md" @click="$router.push({ name: 'login' })">
                        {{ utf8.t('profile.login') }}
                    </ButtonBasic>
                    <ButtonBasic variant="secondary" size="md" @click="$router.push({ name: 'signup' })">
                        {{ utf8.t('profile.signup') }}
                    </ButtonBasic>
                </div>
            </div>
        </div>
    </div>

    <!-- Modal para mensajes -->
    <transition name="fade">
        <div v-if="showModal" class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
            <div class="bg-white rounded-lg shadow-xl max-w-md w-full p-6">
                <h2 class="text-xl font-semibold mb-2">{{ modalTitle }}</h2>
                <p class="mb-4">{{ modalMessage }}</p>
                <ButtonBasic variant="primary" size="md" @click="showModal = false">
                    {{ utf8.t('profile.ok') }}
                </ButtonBasic>
            </div>
        </div>
    </transition>

    <!-- Modal para Stripe Payment -->
    <transition name="fade">
        <div v-if="showStripeModal" class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
            <div class="bg-white rounded-lg shadow-xl max-w-md w-full p-6">
                <div class="flex justify-between items-center mb-4">
                    <h2 class="text-xl font-semibold">{{ utf8.t('profile.premium_subscription') ||
                        'Premium Subscription' }}</h2>
                    <button @click="showStripeModal = false" class="text-gray-500 hover:text-gray-700">
                        <svg class="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                d="M6 18L18 6M6 6l12 12" />
                        </svg>
                    </button>
                </div>
                <div class="mb-6">
                    <p class="text-gray-600 mb-4">{{ utf8.t('profile.premium_description') ||
                        'Upgrade to premium to access exclusive features and benefits.' }}</p>
                    <div class="bg-blue-50 p-4 rounded-lg mb-4">
                        <h3 class="font-semibold text-blue-800 mb-2">{{ utf8.t('profile.premium_benefits') ||
                            'Premium Benefits' }}</h3>
                        <ul class="text-blue-700 space-y-1">
                            <li class="flex items-center">
                                <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 mr-2 text-blue-600" fill="none"
                                    viewBox="0 0 24 24" stroke="currentColor">
                                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                        d="M5 13l4 4L19 7" />
                                </svg>
                                {{ utf8.t('profile.premium_benefit1') || 'Highlighted product listings' }}
                            </li>
                            <li class="flex items-center">
                                <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 mr-2 text-blue-600" fill="none"
                                    viewBox="0 0 24 24" stroke="currentColor">
                                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                        d="M5 13l4 4L19 7" />
                                </svg>
                                {{ utf8.t('profile.premium_benefit2') || 'Priority customer support' }}
                            </li>
                            <li class="flex items-center">
                                <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 mr-2 text-blue-600" fill="none"
                                    viewBox="0 0 24 24" stroke="currentColor">
                                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                        d="M5 13l4 4L19 7" />
                                </svg>
                                {{ utf8.t('profile.premium_benefit3') || 'Advanced analytics' }}
                            </li>
                        </ul>
                    </div>
                </div>

                <StripePayComponent @success="handleSubscriptionSuccess" @cancel="handleSubscriptionCancel" />
            </div>
        </div>
    </transition>
</template>

<script lang="ts">
import ButtonBasic from '@/components/ui/ButtonBasic.vue';
import { useUsers } from '@/composables/useUsers';
import { useutf8Store } from '@/stores/counter';
import { useToast } from 'vue-toastification';
import { defineComponent } from 'vue';
import type { User } from '@/models/user';
import StripePayComponent from '@/components/features/stripe/StripePayComponent.vue';
import axios from 'axios';
import Swal from 'sweetalert2';

export default defineComponent({
    name: 'ProfileViews',
    components: {
        ButtonBasic,
        StripePayComponent,
    },
    data() {
        return {
            utf8: useutf8Store(),
            usersComposable: useUsers(),
            currentUser: null as null | User,
            loading: true,
            error: null as string | null,
            editMode: false,
            updating: false,
            showModal: false,
            modalTitle: '',
            modalMessage: '',
            showStripeModal: false,
            formData: {
                username: '',
                email: '',
                password: '',
                confirmPassword: '',
            },
        };
    },
    computed: {
        isLoggedIn() {
            return !!this.currentUser;
        },
        welcomeMessage() {
            return this.currentUser ? `Hola, ${this.currentUser.username}` : '';
        },
        isAdmin() {
            return this.currentUser?.rol?.name === 'ADMIN';
        },
        isVendedor() {
            return this.currentUser?.rol?.name === 'VENDEDOR';
        },
        isComprador() {
            return this.currentUser?.rol?.name === 'COMPRADOR';
        },
        isTrabajador() {
            return this.currentUser?.rol?.name === 'TRABAJADOR';
        }
    },
    watch: {
        'usersComposable.currentUser': {
            handler(newVal) {
                this.currentUser = newVal;
                if (newVal) {
                    this.resetFormData();
                }
            },
            immediate: true
        }
    },
    mounted() {
        this.checkLoginStatus();
        this.checkPaymentStatus();
    },
    methods: {
        async checkLoginStatus() {
            try {
                this.loading = true;
                this.error = null;
                const userData = await this.usersComposable.isLoggedIn();
                if (!userData) {
                    // No hay error, simplemente no está autenticado
                    this.loading = false;
                    return;
                }
            } catch (error: any) {
                console.error('Error checking login status:', error);
                this.error = error.message || this.utf8.t('profile.login_error');
            } finally {
                this.loading = false;
            }
        },
        resetFormData() {
            if (this.currentUser) {
                this.formData = {
                    username: this.currentUser.username || '',
                    email: this.currentUser.email || '',
                    password: '',
                    confirmPassword: '',
                };
            }
        },
        toggleEditMode() {
            this.editMode = !this.editMode;
            if (this.editMode) {
                this.resetFormData();
            }
        },
        cancelEdit() {
            this.editMode = false;
            this.resetFormData();
        },
        async updateProfile() {
            const toast = useToast();

            // Validar que las contraseñas coincidan si se proporciona una nueva contraseña
            if (this.formData.password && this.formData.password !== this.formData.confirmPassword) {
                toast.error(this.utf8.t('profile.passwords_dont_match'));
                return;
            }

            // Validar formato de email
            if (!this.formData.email.includes('@')) {
                toast.error(this.utf8.t('profile.invalid_email'));
                return;
            }

            try {
                this.updating = true;

                if (!this.currentUser || !this.currentUser.id) {
                    throw new Error('User not authenticated');
                }

                // Preparar datos para actualización
                const updateData: Partial<User> = {
                    username: this.formData.username,
                    email: this.formData.email,
                };

                // Solo incluir la contraseña si se ha proporcionado una nueva
                if (this.formData.password) {
                    updateData.password = this.formData.password;
                }

                // Llamar al método del composable para actualizar el perfil
                await this.usersComposable.updateProfile(this.currentUser.id, updateData);

                this.editMode = false;
                toast.success(this.utf8.t('profile.update_success'));
            } catch (error: any) {
                console.error('Error updating profile:', error);
                toast.error(error.message || this.utf8.t('profile.update_error'));
            } finally {
                this.updating = false;
            }
        },
        showMessage(title: string, message: string) {
            this.modalTitle = title;
            this.modalMessage = message;
            this.showModal = true;
        },
        async logout() {
            try {
                const userComposable = useUsers();
                await userComposable.logout();
            } catch (error) {
                console.error('Error al cerrar sesión:', error);
            }
        },
        checkPaymentStatus() {
            // Verificar si hay un parámetro de éxito o cancelación en la URL
            const urlParams = new URLSearchParams(window.location.search);
            const status = urlParams.get('payment');

            console.log('Checking payment status:', status); // Añadir log para depuración

            if (status === 'success') {
                // Llamar al método para manejar el pago exitoso
                this.handleSubscriptionSuccess();

                // Limpiar la URL de parámetros
                if (window.history.replaceState) {
                    const newUrl = window.location.href.split('?')[0];
                    window.history.replaceState({ path: newUrl }, '', newUrl);
                }
            } else if (status === 'cancelled') {
                // Llamar al método para manejar la cancelación del pago
                this.handleSubscriptionCancel();

                // Limpiar la URL de parámetros
                if (window.history.replaceState) {
                    const newUrl = window.location.href.split('?')[0];
                    window.history.replaceState({ path: newUrl }, '', newUrl);
                }
            }
        },
        async handleSubscriptionSuccess() {
            this.showStripeModal = false;

            // Mostrar SweetAlert2 para el pago exitoso - primero mostrar la alerta
            Swal.fire({
                icon: 'success',
                title: this.utf8.t('profile.subscription_success') || 'Pago realizado con éxito',
                text: this.utf8.t('profile.subscription_success_message') || 'Tu cuenta ha sido actualizada a Premium',
                confirmButtonText: this.utf8.t('common.ok') || 'OK'
            });

            try {
                // Actualizar estado premium en el backend - esto puede complementar el webhook
                // TO-DO: Descomentar y ajustar según la lógica de tu aplicación
                // if (this.currentUser && this.currentUser.id) {
                //     await axios.post(`/api/stripe/update-user-payment`, {
                //         userId: this.currentUser.id,
                //         hasPaid: true
                //     });

                //     // Actualizar el usuario actual en el frontend
                //     this.currentUser.premium = true;
                // }
            } catch (error) {
                console.error('Error al actualizar estado premium:', error);

                Swal.fire({
                    icon: 'error',
                    title: this.utf8.t('common.error') || 'Error',
                    text: this.utf8.t('profile.subscription_update_error') || 'No se pudo actualizar tu estado Premium',
                    confirmButtonText: this.utf8.t('common.ok') || 'OK'
                });
            }
        },
        handleSubscriptionCancel() {
            console.log('Handling subscription cancel'); // Añadir log para depuración
            this.showStripeModal = false;

            Swal.fire({
                icon: 'info',
                title: this.utf8.t('profile.subscription_cancelled') || 'Pago cancelado',
                text: this.utf8.t('profile.subscription_cancelled_message') || 'Has cancelado el proceso de pago',
                confirmButtonText: this.utf8.t('common.ok') || 'OK'
            });
        }
    }
});
</script>

<style scoped>
.fade-enter-active,
.fade-leave-active {
    transition: opacity 0.3s;
}

.fade-enter-from,
.fade-leave-to {
    opacity: 0;
}
</style>
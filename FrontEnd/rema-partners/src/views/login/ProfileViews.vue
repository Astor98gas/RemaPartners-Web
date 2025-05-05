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
</template>

<script lang="ts">
import ButtonBasic from '@/components/ui/ButtonBasic.vue';
import { useUsers } from '@/composables/useUsers';
import { useutf8Store } from '@/stores/counter';
import { useToast } from 'vue-toastification';
import { defineComponent } from 'vue';
import type { User } from '@/models/user';

export default defineComponent({
    name: 'ProfileViews',
    components: {
        ButtonBasic,
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
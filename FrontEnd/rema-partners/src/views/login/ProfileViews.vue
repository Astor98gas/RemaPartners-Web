<template>
    <div class="min-h-screen bg-gray-100 py-8 px-4 sm:px-6 lg:px-8">
        <div class="max-w-[80%] mx-auto">
            <!-- Sección de carga -->
            <div v-if="loading" class="flex justify-center items-center h-64">
                <div class="animate-spin rounded-full h-12 w-12 border-t-2 border-b-2 border-blue-500"></div>
            </div>

            <!-- Sección de error -->
            <div v-else-if="error" class="bg-red-100 border border-red-400 text-re        resetFormData() {
            if (this.currentUser) {
                this.formData = {
                    username: this.currentUser.username || '',
                    email: this.currentUser.email || '',
                    password: '',
                    confirmPassword: '',
                    description: this.currentUser.description || '',
                    profileImage: this.currentUser.profileImage || '',
                    socialLinks: this.currentUser.socialLinks ? 
                        [...this.currentUser.socialLinks] : []
                };
                this.previewImage = this.currentUser.profileImage || null;
            }
        }, py-3 rounded-lg mb-6">
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
                                <!-- Imagen de perfil y nombre de usuario -->
                                <div class="flex flex-col items-center mb-6">
                                    <div
                                        class="w-32 h-32 rounded-full overflow-hidden border-4 border-blue-500 mb-3 bg-white">
                                        <img v-if="currentUser.profileImage" :src="currentUser.profileImage"
                                            :alt="currentUser.username" class="w-full h-full object-cover"
                                            @error="onImageError">
                                        <div v-else
                                            class="w-full h-full flex items-center justify-center bg-gray-100 text-gray-400">
                                            <svg xmlns="http://www.w3.org/2000/svg" class="h-16 w-16" fill="none"
                                                viewBox="0 0 24 24" stroke="currentColor">
                                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                                    d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z" />
                                            </svg>
                                        </div>
                                    </div>
                                    <h3 class="text-2xl font-bold text-gray-800">{{ currentUser.username }}</h3>
                                    <span class="text-sm bg-blue-100 text-blue-800 px-3 py-1 rounded-full mt-1">
                                        {{ currentUser.rol?.name || 'N/A' }}
                                    </span>
                                </div>

                                <!-- Email -->
                                <div>
                                    <p class="text-sm text-gray-500">{{ utf8.t('profile.email') }}</p>
                                    <p class="text-lg font-medium">{{ currentUser.email }}</p>
                                </div>

                                <!-- Descripción del perfil -->
                                <div>
                                    <p class="text-sm text-gray-500">{{ utf8.t('profile.description') }}</p>
                                    <p class="text-lg font-medium whitespace-pre-line">{{ currentUser.description ||
                                        utf8.t('profile.no_description') }}</p>
                                </div>

                                <!-- Enlaces a redes sociales -->
                                <div v-if="currentUser.socialLinks && currentUser.socialLinks.length > 0">
                                    <p class="text-sm text-gray-500 mb-2">{{ utf8.t('profile.social_links') }}</p>
                                    <div class="flex flex-wrap gap-3">
                                        <a v-for="(link, index) in currentUser.socialLinks" :key="index"
                                            :href="link.url" target="_blank" rel="noopener noreferrer"
                                            class="px-3 py-2 bg-gray-100 hover:bg-gray-200 text-gray-800 rounded-lg flex items-center transition-colors">
                                            <i v-if="link.icon" :class="link.icon" class="mr-2"></i>
                                            <span>{{ link.platform }}</span>
                                        </a>
                                    </div>
                                </div>

                                <ButtonBasic variant="primary" size="md" @click="toggleEditMode">
                                    {{ utf8.t('profile.edit_profile') }}
                                </ButtonBasic>
                            </div>

                            <form v-else @submit.prevent="updateProfile" class="space-y-4">
                                <!-- Imagen de perfil -->
                                <div class="flex flex-col items-center mb-4">
                                    <div
                                        class="w-32 h-32 rounded-full overflow-hidden border-4 border-blue-500 mb-3 bg-white relative group">
                                        <img v-if="previewImage || currentUser.profileImage"
                                            :src="previewImage || currentUser.profileImage" :alt="formData.username"
                                            class="w-full h-full object-cover" @error="onImageError">
                                        <div v-else
                                            class="w-full h-full flex items-center justify-center bg-gray-100 text-gray-400">
                                            <svg xmlns="http://www.w3.org/2000/svg" class="h-16 w-16" fill="none"
                                                viewBox="0 0 24 24" stroke="currentColor">
                                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                                    d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z" />
                                            </svg>
                                        </div>
                                        <div
                                            class="absolute inset-0 bg-black bg-opacity-50 opacity-0 group-hover:opacity-100 flex items-center justify-center transition-opacity duration-200">
                                            <label for="profile-image-upload"
                                                class="text-white cursor-pointer px-3 py-1 bg-blue-600 rounded-lg text-sm hover:bg-blue-700">
                                                Cambiar
                                            </label>
                                        </div>
                                    </div>
                                    <input type="file" id="profile-image-upload" @change="handleImageUpload"
                                        class="hidden" accept="image/*">
                                </div>

                                <!-- Nombre de usuario -->
                                <div>
                                    <label class="block text-sm text-gray-500">{{ utf8.t('profile.username') }}</label>
                                    <input type="text" v-model="formData.username"
                                        class="w-full px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
                                        required>
                                </div>

                                <!-- Email -->
                                <div>
                                    <label class="block text-sm text-gray-500">{{ utf8.t('profile.email') }}</label>
                                    <input type="email" v-model="formData.email"
                                        class="w-full px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
                                        required>
                                </div>

                                <!-- Descripción -->
                                <div>
                                    <label class="block text-sm text-gray-500">{{ utf8.t('profile.description')
                                    }}</label>
                                    <textarea v-model="formData.description" rows="4"
                                        class="w-full px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500 resize-none"
                                        :placeholder="utf8.t('profile.description_placeholder')">
                                    </textarea>
                                </div>

                                <!-- Redes sociales -->
                                <div>
                                    <label class="block text-sm text-gray-500 mb-2">{{ utf8.t('profile.social_links')
                                    }}</label>

                                    <div v-for="(link, index) in formData.socialLinks" :key="index"
                                        class="flex gap-2 mb-2">
                                        <select v-model="link.platform"
                                            class="px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500">
                                            <option value="Facebook">Facebook</option>
                                            <option value="Twitter">Twitter</option>
                                            <option value="Instagram">Instagram</option>
                                            <option value="LinkedIn">LinkedIn</option>
                                            <option value="GitHub">GitHub</option>
                                            <option value="Website">Website</option>
                                        </select>
                                        <input type="url" v-model="link.url" placeholder="https://..."
                                            class="flex-1 px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500">
                                        <button type="button" @click="removeLink(index)"
                                            class="px-2 py-2 bg-red-500 text-white rounded-lg hover:bg-red-600">
                                            <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" fill="none"
                                                viewBox="0 0 24 24" stroke="currentColor">
                                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                                    d="M6 18L18 6M6 6l12 12" />
                                            </svg>
                                        </button>
                                    </div>

                                    <button type="button" @click="addLink"
                                        class="mt-2 px-4 py-2 bg-gray-200 text-gray-800 rounded-lg hover:bg-gray-300 flex items-center">
                                        <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 mr-2" fill="none"
                                            viewBox="0 0 24 24" stroke="currentColor">
                                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                                d="M12 4v16m8-8H4" />
                                        </svg>
                                        {{ utf8.t('profile.add_social_link') }}
                                    </button>
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
                                    <router-link v-if="isVendedor || isAdmin || isTrabajador" to="/dashboard"
                                        class="text-blue-600 hover:text-blue-800 block py-1">
                                        {{ utf8.t('profile.dashboard') }}
                                    </router-link>
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
                                    <router-link to="/facturas" class="text-blue-600 hover:text-blue-800 block py-1">
                                        {{ utf8.t('profile.view_invoices') }}
                                    </router-link>
                                </div>

                                <!-- Subscription Section -->
                                <div v-if="isComprador || isAdmin || isTrabajador" class="border-b pb-4">
                                    <h3 class="font-medium mb-2">{{ utf8.t('profile.subscription') || 'Subscription' }}
                                    </h3>
                                    <ButtonBasic variant="success" size="md" @click="showSubscriptionOptions = true"
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

    <!-- Modal para elección de suscripción -->
    <transition name="fade">
        <div v-if="showSubscriptionOptions"
            class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
            <div class="bg-white rounded-lg shadow-xl max-w-md w-full p-6">
                <div class="flex justify-between items-center mb-4">
                    <h2 class="text-xl font-semibold">{{ utf8.t('profile.subscription_options') ||
                        'Opciones de Suscripción' }}</h2>
                    <button @click="showSubscriptionOptions = false" class="text-gray-500 hover:text-gray-700">
                        <svg class="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                d="M6 18L18 6M6 6l12 12" />
                        </svg>
                    </button>
                </div>
                <div class="mb-6">
                    <p class="text-gray-600 mb-4">
                        {{ utf8.t('profile.subscription_options_desc') ||
                            'Elige una opción de suscripción para convertirte en vendedor.' }}
                    </p>

                    <div class="space-y-4">
                        <div v-if="!hasHadSubscription"
                            class="border border-blue-200 rounded-lg p-4 hover:bg-blue-50 transition cursor-pointer"
                            @click="createFreeTrial">
                            <h3 class="font-semibold text-blue-800 mb-1">
                                {{ utf8.t('profile.free_trial') || 'Prueba Gratuita' }}
                            </h3>
                            <p class="text-sm text-gray-600">
                                {{ utf8.t('profile.free_trial_desc') ||
                                    'Obtén acceso a funciones de vendedor durante 30 días sin costo.' }}
                            </p>
                        </div>

                        <div class="border border-blue-200 rounded-lg p-4 hover:bg-blue-50 transition cursor-pointer"
                            @click="startPremiumSubscription">
                            <h3 class="font-semibold text-blue-800 mb-1">
                                {{ utf8.t('profile.premium_subscription') || 'Suscripción Premium' }}
                            </h3>
                            <p class="text-sm text-gray-600">
                                {{ utf8.t('profile.premium_desc') ||
                                    'Suscríbete para obtener todas las funciones premium y renovación automática.' }}
                            </p>
                        </div>
                    </div>
                </div>
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
import type { User, SocialLink } from '@/models/user';
import StripePayComponent from '@/components/features/stripe/StripePayComponent.vue';
import FacturasPanel from '@/components/profile/FacturasPanel.vue';
import axios from 'axios';
import Swal from 'sweetalert2';

export default defineComponent({
    name: 'ProfileViews',
    components: {
        ButtonBasic,
        StripePayComponent,
        FacturasPanel,
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
            showSubscriptionOptions: false,
            hasHadSubscription: false,
            formData: {
                username: '',
                email: '',
                password: '',
                confirmPassword: '',
                description: '',
                profileImage: '',
                socialLinks: [] as SocialLink[],
            },
            previewImage: null as string | null,
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
                await this.checkSubscriptionHistory();
            } catch (error: any) {
                console.error('Error checking login status:', error);
                this.error = error.message || this.utf8.t('profile.login_error');
            } finally {
                this.loading = false;
            }
        },
        async checkSubscriptionHistory() {
            try {
                if (!this.currentUser || !this.currentUser.id) return;

                const response = await axios.get(`/api/stripe/check-subscription-history/${this.currentUser.id}`);
                this.hasHadSubscription = response.data.hasHadSubscription;
            } catch (error) {
                console.error('Error checking subscription history:', error);
            }
        },
        resetFormData() {
            if (this.currentUser) {
                this.formData = {
                    username: this.currentUser.username || '',
                    email: this.currentUser.email || '',
                    password: '',
                    confirmPassword: '',
                    description: this.currentUser.description || '',
                    profileImage: this.currentUser.profileImage || '',
                    socialLinks: this.currentUser.socialLinks ?
                        [...this.currentUser.socialLinks] : []
                };
                this.previewImage = this.currentUser.profileImage || null;
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

                // Check if username is being changed
                const isUsernameChanged = this.currentUser.username !== this.formData.username;

                // Preparar datos para actualización
                const updateData: Partial<User> = {
                    username: this.formData.username,
                    email: this.formData.email,
                    description: this.formData.description,
                    profileImage: this.formData.profileImage,
                    socialLinks: this.formData.socialLinks
                };

                // Solo incluir la contraseña si se ha proporcionado una nueva
                if (this.formData.password) {
                    updateData.password = this.formData.password;
                }

                // Save current password for re-login if needed
                const currentPassword = this.formData.password || '';

                // Llamar al método del composable para actualizar el perfil
                await this.usersComposable.updateProfile(this.currentUser.id, updateData);

                // If username was changed, we need to logout and login again
                if (isUsernameChanged) {
                    toast.info(this.utf8.t('profile.username_changed_relogin') || 'Username changed. You will be logged in again.');

                    // First logout to invalidate current token
                    await this.usersComposable.logout(false); // Pass false to prevent redirect

                    // Then login with new credentials
                    if (this.formData.password) {
                        // If password was also changed, use new password
                        await this.usersComposable.loginUser({
                            username: this.formData.username,
                            password: this.formData.password
                        });
                    } else {
                        // Show modal asking for password to re-login
                        await Swal.fire({
                            title: this.utf8.t('profile.relogin_required') || 'Re-login Required',
                            text: this.utf8.t('profile.username_changed_enter_password') || 'Your username was changed. Please enter your password to log in with the new username.',
                            input: 'password',
                            inputAttributes: {
                                autocapitalize: 'off',
                                autocorrect: 'off'
                            },
                            showCancelButton: true,
                            confirmButtonText: this.utf8.t('common.login') || 'Login',
                            showLoaderOnConfirm: true,
                            preConfirm: async (password) => {
                                try {
                                    await this.usersComposable.loginUser({
                                        username: this.formData.username,
                                        password: password
                                    });
                                    return true;
                                } catch (error: any) {
                                    Swal.showValidationMessage(
                                        error.response?.data?.message || 'Login failed'
                                    );
                                    return false;
                                }
                            }
                        }).then((result) => {
                            if (result.isConfirmed) {
                                // Refresh page after successful re-login
                                window.location.reload();
                            } else {
                                // If canceled, redirect to login page
                                this.$router.push({ name: 'login' });
                            }
                        });
                    }
                } else {
                    this.editMode = false;
                    toast.success(this.utf8.t('profile.update_success'));
                }
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

            try {
                // Mostrar indicador de carga
                Swal.fire({
                    title: this.utf8.t('profile.processing') || 'Procesando...',
                    allowOutsideClick: false,
                    didOpen: () => {
                        Swal.showLoading();
                    }
                });

                // Asegurarse de que el usuario esté autenticado
                await this.checkLoginStatus();

                if (!this.currentUser || !this.currentUser.id) {
                    console.error('No hay usuario autenticado o falta el ID de usuario');
                    throw new Error('Usuario no identificado');
                }

                // Obtener el token de auth
                const token = document.cookie
                    .split('; ')
                    .find(row => row.startsWith('token='))
                    ?.split('=')[1];

                console.log('ID del usuario:', this.currentUser.id);
                console.log('Token disponible:', !!token);

                // Intentar con axios directamente con headers mejorados
                const response = await axios.post('/api/stripe/update-user-payment', {
                    userId: this.currentUser.id,
                    hasPaid: true
                }, {
                    timeout: 30000, // Extender timeout a 30 segundos
                    headers: {
                        'Content-Type': 'application/json',
                        'Authorization': token ? `Bearer ${token}` : '',
                    }
                });

                console.log('Respuesta del servidor:', response.data);

                // El resto del código sigue igual
                if (response.data && response.data.success) {
                    // Actualizar el usuario en el frontend para reflejar el nuevo rol
                    await this.usersComposable.refreshUser();

                    // Cerrar el modal de carga
                    Swal.close();

                    // Mostrar mensaje de éxito
                    Swal.fire({
                        icon: 'success',
                        title: this.utf8.t('profile.subscription_success') || 'Pago realizado con éxito',
                        text: this.utf8.t('profile.subscription_success_message') || 'Tu cuenta ha sido actualizada a Premium',
                        confirmButtonText: this.utf8.t('common.ok') || 'OK'
                    });
                } else {
                    throw new Error(response.data?.message || 'Error en la respuesta del servidor');
                }
            } catch (error: any) {
                console.error('Error al actualizar estado premium:', error);
                console.error('Detalles del error:', error.response?.data || 'No hay detalles adicionales');

                // Cerrar el modal de carga si está abierto
                Swal.close();

                Swal.fire({
                    icon: 'error',
                    title: this.utf8.t('common.error') || 'Error',
                    text: error.response?.data?.message || error.message || this.utf8.t('profile.subscription_update_error'),
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
        },
        async createFreeTrial() {
            try {
                this.showSubscriptionOptions = false;

                // Mostrar indicador de carga
                Swal.fire({
                    title: this.utf8.t('profile.processing') || 'Procesando...',
                    allowOutsideClick: false,
                    didOpen: () => {
                        Swal.showLoading();
                    }
                });

                if (!this.currentUser || !this.currentUser.id) {
                    throw new Error('User not authenticated');
                }

                // Llamada a la API para crear suscripción gratuita
                const response = await axios.post('/api/stripe/create-free-trial', {
                    userId: this.currentUser.id
                });

                if (response.data && response.data.success) {
                    // Actualizar el usuario en el frontend para reflejar el nuevo rol
                    await this.usersComposable.refreshUser();

                    // Mostrar mensaje de éxito
                    Swal.fire({
                        icon: 'success',
                        title: this.utf8.t('profile.free_trial_success') || '¡Prueba activada!',
                        text: this.utf8.t('profile.free_trial_success_message') || 'Tu cuenta ha sido actualizada a vendedor por 30 días.',
                        confirmButtonText: this.utf8.t('common.ok') || 'OK'
                    });
                } else {
                    throw new Error(response.data?.message || 'Error activando la prueba gratuita');
                }
            } catch (error: any) {
                console.error('Error creating free trial:', error);

                Swal.fire({
                    icon: 'error',
                    title: this.utf8.t('common.error') || 'Error',
                    text: error.response?.data?.message || error.message || this.utf8.t('profile.subscription_update_error'),
                    confirmButtonText: this.utf8.t('common.ok') || 'OK'
                });
            }
        },
        startPremiumSubscription() {
            console.log('Starting premium subscription'); // Añadir log para depuración
            this.showSubscriptionOptions = false;
            this.showStripeModal = true;
        },
        toggleSubscriptionOption() {
            this.showSubscriptionOptions = true;
        },
        onImageError(event: Event) {
            // Fallback image for profile image loading errors
            (event.target as HTMLImageElement).src = new URL('@/assets/logoCuadrado.jpeg', import.meta.url).href;
        },

        handleImageUpload(event: Event) {
            const file = (event.target as HTMLInputElement).files?.[0];
            if (!file) return;

            // Verify it's an image
            if (!file.type.startsWith('image/')) {
                const toast = useToast();
                toast.error(this.utf8.t('profile.image_type_error') || 'Please select an image file');
                return;
            }

            // Create a preview URL
            this.previewImage = URL.createObjectURL(file);

            // Convert to base64 for storing
            const reader = new FileReader();
            reader.onload = (e) => {
                if (e.target) {
                    this.formData.profileImage = e.target.result as string;
                }
            };
            reader.readAsDataURL(file);
        },

        addLink() {
            if (!this.formData.socialLinks) {
                this.formData.socialLinks = [];
            }

            this.formData.socialLinks.push({
                platform: '',
                url: '',
                icon: ''
            });
        },

        removeLink(index: number) {
            if (this.formData.socialLinks && this.formData.socialLinks.length > index) {
                this.formData.socialLinks.splice(index, 1);
            }
        },
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
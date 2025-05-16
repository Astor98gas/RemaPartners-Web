<template>
    <div class="flex flex-col h-full sticky top-0 shadow-xl">
        <!-- Sidebar con clase dinámica para el ancho -->
        <aside
            class="bg-gradient-to-b from-gray-800 to-gray-900 text-white flex flex-col h-full transition-all duration-300"
            :class="isCollapsed ? 'w-16' : 'w-64'">
            <!-- Header -->
            <header class="px-6 py-8 border-b border-gray-700 flex justify-between items-center">
                <!-- Botón para colapsar/expandir -->
                <button @click="toggleSidebar"
                    class="text-gray-400 hover:text-white focus:outline-none transition-transform duration-300"
                    :class="{ 'rotate-180': isCollapsed }">
                    <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" fill="none" viewBox="0 0 24 24"
                        stroke="currentColor">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                            d="M11 19l-7-7 7-7m8 14l-7-7 7-7" />
                    </svg>
                </button>
            </header>

            <!-- Navigation -->
            <nav class="flex-1 px-4 py-6 overflow-y-auto">
                <h3 class="text-xs uppercase text-gray-500 font-semibold mb-3 px-4 transition-opacity duration-300"
                    :class="{ 'opacity-0': isCollapsed }">
                    {{ isCollapsed ? '' : utf8.t('navigation.dynamic') }}
                </h3>
                <ul class="space-y-1">
                    <li v-for="link in filteredLinks" :key="link.text">
                        <router-link :to="link.href"
                            class="flex items-center px-4 py-3 text-gray-300 rounded-lg transition-all duration-200 group"
                            :class="{
                                'bg-gray-300 text-gray-700': currentPath === link.href && !isCollapsed,
                                'hover:bg-gray-700 hover:text-white': currentPath !== link.href || isCollapsed,
                                'justify-center': isCollapsed
                            }" :title="link.text">
                            <!-- Icon placeholder (you can add specific icons for each link) -->
                            <svg v-if="isCollapsed" xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" fill="none"
                                viewBox="0 0 24 24" stroke="currentColor">
                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                    d="M4 6h16M4 12h16M4 18h7" />
                            </svg>

                            <span class="flex-1 transition-opacity duration-300" :class="{ 'hidden': isCollapsed }">
                                {{ link.text }}
                            </span>

                            <svg v-if="currentPath === link.href && !isCollapsed" class="w-5 h-5"
                                xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24"
                                stroke="currentColor">
                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                    d="M9 5l7 7-7 7" />
                            </svg>
                        </router-link>
                    </li>
                </ul>
            </nav>

            <!-- Static Navigation -->
            <div class="px-6 py-4 border-t border-gray-700">
                <h3 class="text-xs uppercase text-gray-500 font-semibold mb-3 px-4 transition-opacity duration-300"
                    :class="{ 'opacity-0': isCollapsed }">
                    {{ isCollapsed ? '' : utf8.t('navigation.static') }}
                </h3>
                <nav class="px-4 overflow-y-auto">
                    <ul class="space-y-1">
                        <li v-for="link in staticLinks" :key="link.text">
                            <router-link :to="link.href"
                                class="flex items-center px-4 py-3 text-gray-300 rounded-lg transition-all duration-200 group"
                                :class="{
                                    'bg-gray-300 text-gray-700': currentPath === link.href && !isCollapsed,
                                    'hover:bg-gray-700 hover:text-white': currentPath !== link.href || isCollapsed,
                                    'justify-center': isCollapsed
                                }" :title="link.text">
                                <!-- Icon placeholder (you can add specific icons for each link) -->
                                <svg v-if="isCollapsed" xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" fill="none"
                                    viewBox="0 0 24 24" stroke="currentColor">
                                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                        d="M4 6h16M4 12h16M4 18h7" />
                                </svg>

                                <span class="flex-1 transition-opacity duration-300" :class="{ 'hidden': isCollapsed }">
                                    {{ link.text }}
                                </span>

                                <svg v-if="currentPath === link.href && !isCollapsed" class="w-5 h-5"
                                    xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24"
                                    stroke="currentColor">
                                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                        d="M9 5l7 7-7 7" />
                                </svg>
                            </router-link>
                        </li>
                    </ul>
                </nav>
            </div>

            <!-- Footer -->
            <footer class="px-6 py-4 text-gray-400 text-sm border-t border-gray-700 transition-opacity duration-300"
                :class="{ 'opacity-0': isCollapsed }">
                <p v-if="!isCollapsed" class="flex justify-between">
                    <span>&copy; {{ currentDate }} Arsansys</span>
                    <span>v1.0.0</span>
                </p>
            </footer>
        </aside>
    </div>
</template>

<script lang="ts">
import { defineComponent, watch } from 'vue';
import { useRoute } from 'vue-router';
import { useutf8Store } from '@/stores/counter';
import { useUsers } from '@/composables/useUsers';

export default defineComponent({
    name: 'LateralHeaderView',
    data() {
        return {
            isCollapsed: false, // Nueva propiedad para el estado de colapso
            links: [] as Array<{ text: string; href: string; visibleOn?: string[]; requiresAuth?: boolean; roles?: string[] }>,
            currentPath: '',
            currentLanguage: '',
            currentDate: new Date().toLocaleDateString('es-ES', {
                year: 'numeric'
            }),
            utf8: useutf8Store(),
            currentUser: null,
            isLoggedIn: false,
            userRole: null
        };
    },
    computed: {
        filteredLinks() {
            return this.links.filter(link => {
                // First check authentication requirements
                if (link.requiresAuth && !this.isLoggedIn) {
                    return false;
                }

                // Then check role requirements
                if (link.roles && this.isLoggedIn && this.userRole && !link.roles.includes(this.userRole)) {
                    return false;
                }

                // Apply existing filtering logic for dynamic sections
                if (!link.visibleOn) {
                    return false; // No mostrar en navegación dinámica
                }

                return link.visibleOn.some(pattern => {
                    if (pattern.includes('*')) {
                        const regex = new RegExp('^' + pattern.replace('*', '.*') + '$');
                        return regex.test(this.currentPath);
                    } else {
                        return pattern === this.currentPath;
                    }
                });
            });
        },

        staticLinks() {
            // Filter static links by authentication and role requirements
            return this.links.filter(link => {
                // First check authentication requirements
                if (link.requiresAuth && !this.isLoggedIn) {
                    return false;
                }

                // Then check role requirements
                if (link.roles && this.isLoggedIn && this.userRole && !link.roles.includes(this.userRole)) {
                    return false;
                }

                // Original static link filtering
                return !link.visibleOn;
            });
        },

        t(key: string): string {
            const store = useutf8Store();
            return store.t(key);
        }
    },
    created() {
        // Set current path and language
        const route = useRoute();
        this.currentPath = route.path;
        const utf8Store = useutf8Store();
        this.currentLanguage = utf8Store.currentLanguage;

        // Check login status
        this.checkLoginStatus();

        // Cargar estado de colapso del local storage si existe
        const savedState = localStorage.getItem('sidebarCollapsed');
        if (savedState) {
            this.isCollapsed = savedState === 'true';
        }
    },
    mounted() {
        // Observar cambios en la ruta
        this.$watch(
            () => this.$route.path,
            (newPath) => {
                this.currentPath = newPath;
            }
        );

        // Observar cambios en el idioma
        const utf8Store = useutf8Store();
        watch(
            () => utf8Store.currentLanguage,
            (newLanguage) => {
                this.currentLanguage = newLanguage;
                this.loadLinks();
            }
        );

        // Watch for auth changes
        const usersComposable = useUsers();
        watch(
            () => usersComposable.currentUser.value,
            () => {
                this.checkLoginStatus();
            }
        );
    },
    methods: {
        // Método para alternar el estado de la barra lateral
        toggleSidebar() {
            this.isCollapsed = !this.isCollapsed;
            // Guardar estado en localStorage para persistencia
            localStorage.setItem('sidebarCollapsed', this.isCollapsed.toString());
        },

        async checkLoginStatus() {
            try {
                const usersComposable = useUsers();
                const userData = await usersComposable.isLoggedIn();
                this.currentUser = userData;
                this.isLoggedIn = !!userData;
                this.userRole = userData?.rol?.name || null;
            } catch (error) {
                this.isLoggedIn = false;
                this.currentUser = null;
                this.userRole = null;
            } finally {
                this.loadLinks(); // Reload links after checking login status
            }
        },

        loadLinks() {
            const utf8 = useutf8Store();
            this.links = [
                {
                    text: utf8.t('route.home'),
                    href: '/',
                },
                {
                    text: utf8.t('links.producto.add'),
                    href: '/producto/create',
                    visibleOn: ['/producto/*', '/'],
                    requiresAuth: true,
                    roles: ['VENDEDOR', 'ADMIN']
                },
                {
                    text: utf8.t('links.settings'),
                    href: '/settings',
                    visibleOn: ['/settings'],
                    requiresAuth: true
                },
                {
                    text: utf8.t('links.categoria.add'),
                    href: '/admin/categoria/create',
                    visibleOn: ['/admin/categoria/*'],
                    requiresAuth: true,
                    roles: ['ADMIN']
                },
                {
                    text: utf8.t('links.categoria.list'),
                    href: '/admin/categoria/list',
                    requiresAuth: true,
                    roles: ['ADMIN']
                },
                {
                    text: utf8.t('links.profile'),
                    href: '/profile',
                    requiresAuth: true
                },
                {
                    text: utf8.t('links.chat'),
                    href: '/chats',
                    requiresAuth: true,
                    roles: ['COMPRADOR', 'ADMIN', 'VENDEDOR']
                },
                {
                    text: utf8.t('links.invoices'),
                    href: '/facturas',
                    requiresAuth: true,
                    roles: ['COMPRADOR', 'ADMIN', 'VENDEDOR']
                },
                {
                    text: utf8.t('links.help'),
                    href: '/help'
                },
            ];
        },
        reloadLinks() {
            this.loadLinks();
        }
    }
});
</script>

<style scoped>
/* Añadir cualquier estilo específico que necesites aquí */
</style>
<template>
    <div class="flex flex-col h-full sticky top-0 shadow-xl">
        <!-- Sidebar -->
        <aside class="bg-gradient-to-b from-gray-800 to-gray-900 text-white w-64 flex flex-col h-full">
            <!-- Header -->
            <header class="px-6 py-8 border-b border-gray-700">
                <div class="flex items-center space-x-3">
                    <img src="@/assets/logo.png" alt="Logo" class="h-10 w-10" />
                    <h1 class="text-xl font-bold tracking-wider">REMA Partners</h1>
                </div>
            </header>

            <!-- Navigation -->
            <nav class="flex-1 px-4 py-6 overflow-y-auto">
                <h3 class="text-xs uppercase text-gray-500 font-semibold mb-3 px-4">{{ utf8.t('navigation.dynamic')
                    }}</h3>
                <ul class="space-y-1">
                    <li v-for="link in filteredLinks" :key="link.text">
                        <router-link :to="link.href"
                            class="flex items-center px-4 py-3 text-gray-300 rounded-lg transition-all duration-200 group"
                            :class="{
                                'bg-gray-300 text-gray-700': currentPath === link.href,
                                'hover:bg-gray-700 hover:text-white': currentPath !== link.href
                            }">
                            <span class="flex-1">{{ link.text }}</span>
                            <svg v-if="currentPath === link.href" class="w-5 h-5" xmlns="http://www.w3.org/2000/svg"
                                fill="none" viewBox="0 0 24 24" stroke="currentColor">
                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                    d="M9 5l7 7-7 7" />
                            </svg>
                        </router-link>
                    </li>
                </ul>
            </nav>

            <!-- Static Navigation -->
            <div class="px-6 py-4 border-t border-gray-700">
                <h3 class="text-xs uppercase text-gray-500 font-semibold mb-3 px-4">{{ utf8.t('navigation.static') }}
                </h3>
                <nav class="px-4 overflow-y-auto">
                    <ul class="space-y-1">
                        <li v-for="link in staticLinks" :key="link.text">
                            <router-link :to="link.href"
                                class="flex items-center px-4 py-3 text-gray-300 rounded-lg transition-all duration-200 group"
                                :class="{
                                    'bg-gray-300 text-gray-700': currentPath === link.href,
                                    'hover:bg-gray-700 hover:text-white': currentPath !== link.href
                                }">
                                <span class="flex-1">{{ link.text }}</span>
                                <svg v-if="currentPath === link.href" class="w-5 h-5" xmlns="http://www.w3.org/2000/svg"
                                    fill="none" viewBox="0 0 24 24" stroke="currentColor">
                                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                        d="M9 5l7 7-7 7" />
                                </svg>
                            </router-link>
                        </li>
                    </ul>
                </nav>
            </div>

            <!-- Footer -->
            <footer class="px-6 py-4 text-gray-400 text-sm border-t border-gray-700">
                <p class="flex justify-between">
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

export default defineComponent({
    name: 'LateralHeaderView',
    data() {
        return {
            links: [] as Array<{ text: string; href: string; visibleOn?: string[] }>,
            currentPath: '',
            currentLanguage: '',
            currentDate: new Date().toLocaleDateString('es-ES', {
                year: 'numeric'
            }),
            utf8: useutf8Store(),
        };
    },
    computed: {
        filteredLinks() {
            return this.links.filter(link => {
                // Solo mostrar en la sección dinámica los enlaces con visibleOn
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
            // Mostrar solo enlaces sin propiedad visibleOn en la navegación estática
            return this.links.filter(link => !link.visibleOn);
        },

        t(key: string): string {
            const store = useutf8Store();
            return store.t(key);
        }
    },
    created() {
        // Cargar los enlaces inicialmente
        this.loadLinks();

        // Configurar el path actual
        const route = useRoute();
        this.currentPath = route.path;

        // Configurar el idioma actual
        const utf8Store = useutf8Store();
        this.currentLanguage = utf8Store.currentLanguage;
    },
    mounted() {
        // Observar cambios en la ruta
        this.$watch(
            () => this.$route.path,
            (newPath) => {
                this.currentPath = newPath;
                this.loadLinks();
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
    },
    methods: {
        loadLinks() {
            const utf8 = useutf8Store();
            this.links = [
                { text: utf8.t('links.dashboard'), href: '/dashboard', visibleOn: ['/dashboard', '/home'] },
                { text: utf8.t('links.settings'), href: '/settings', visibleOn: ['/settings'] },
                { text: utf8.t('links.categoria.add'), href: '/admin/categoria/create', visibleOn: ['/admin/categoria/*'] },
                { text: utf8.t('links.categoria.list'), href: '/admin/categoria/list' },
                { text: utf8.t('links.profile'), href: '/profile' },
                { text: utf8.t('links.help'), href: '/help' },
            ];
        },
        reloadLinks() {
            this.loadLinks();
        }
    }
});
</script>
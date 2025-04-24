<template>
    <div class="flex flex-col h-full sticky top-0">
        <!-- Sidebar -->
        <aside class="bg-gray-800 text-white w-64 flex flex-col h-full">
            <header class="p-4">
                <h1 class="text-2xl font-bold">Lateral Header</h1>
            </header>
            <nav class="flex-1 p-4 overflow-y-auto">
                <ul>
                    <li v-for="link in filteredLinks" :key="link.text" class="mb-2">
                        <router-link :to="link.href" class="text-white hover:underline">
                            {{ link.text }}
                        </router-link>
                    </li>
                </ul>
            </nav>
            <footer class="p-4">
                <p>&copy; 2025 Arsansys</p>
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
            currentLanguage: ''
        };
    },
    computed: {
        filteredLinks() {
            return this.links.filter(link => {
                if (!link.visibleOn) {
                    return true;
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
                { text: utf8.t('links.categoria.list'), href: '/admin/categoria/list', visibleOn: ['/admin/categoria/*'] },
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
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
                        <a :href="link.href" class="text-white hover:underline">{{ link.text }}</a>
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
import { defineComponent } from 'vue';
import { useRoute } from 'vue-router';
import { useutf8Store } from '@/stores/counter';

export default defineComponent({
    name: 'LateralHeaderView',
    data() {
        return {
            links: [] as Array<{ text: string; href: string; visibleOn?: string[] }>
        };
    },
    computed: {
        filteredLinks() {
            const route = useRoute();
            const currentPath = String(route.name || '');
            return this.links.filter(link => !link.visibleOn || link.visibleOn.includes(currentPath));
        }
    },
    created() {
        const utf8 = useutf8Store();
        this.links = [
            { text: utf8.t('links.dashboard'), href: '/dashboard', visibleOn: ['dashboard', 'home'] },
            { text: utf8.t('links.settings'), href: '/settings', visibleOn: ['settings'] },
            { text: utf8.t('links.profile'), href: '/profile' },
            { text: utf8.t('links.help'), href: '/help' },
        ];
    }
});
</script>
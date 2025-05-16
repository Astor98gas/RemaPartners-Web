<template>
    <div class="min-h-screen bg-gray-100 py-8 px-4 sm:px-6 lg:px-8">
        <div class="max-w-[80%] mx-auto">
            <div class="bg-white shadow overflow-hidden rounded-lg">
                <!-- Cabecera -->
                <div class="bg-gradient-to-r from-blue-500 to-indigo-600 px-6 py-5">
                    <h1 class="text-2xl font-semibold text-white">{{ utf8.t('invoice.title') }}</h1>
                </div>

                <!-- Contenido principal -->
                <div class="p-6">
                    <!-- Componente de visualización de facturas -->
                    <div v-if="loading" class="flex justify-center items-center h-36">
                        <div class="animate-spin rounded-full h-8 w-8 border-t-2 border-b-2 border-blue-500"></div>
                    </div>
                    <div v-else-if="error" class="text-red-500 text-center p-6">
                        {{ error }}
                    </div>
                    <div v-else>
                        <FacturasPanel :userId="currentUser?.id?.toString() || ''" />
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue';
import { useUsers } from '@/composables/useUsers';
import { useutf8Store } from '@/stores/counter';
import FacturasPanel from '@/components/profile/FacturasPanel.vue';

export default defineComponent({
    name: 'FacturasView',
    components: {
        FacturasPanel
    },
    data() {
        return {
            utf8: useutf8Store(),
            currentUser: null as { id: string | number } | null,
            loading: true,
            error: null
        };
    },
    async mounted() {
        await this.loadUserData();
    },
    methods: {
        async loadUserData() {
            try {
                this.loading = true;
                const usersComposable = useUsers();
                const userData = await usersComposable.isLoggedIn();
                this.currentUser = userData;
            } catch (error: any) {
                console.error('Error loading user data:', error);
                this.error = error.message || 'Error loading user data';
            } finally {
                this.loading = false;
            }
        }
    }
});
</script>

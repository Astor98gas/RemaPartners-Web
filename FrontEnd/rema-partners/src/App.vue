<script lang="ts">
import { defineComponent } from 'vue';
import HeaderView from '@/views/HeaderView.vue';
import LateralHeaderView from '@/views/LateralHeaderView.vue';

export default defineComponent({
  name: 'App',
  components: {
    HeaderView,
    LateralHeaderView
  }
});
</script>


<!-- Componente principal de la aplicación.
Actúa como contenedor raíz para todos los demás componentes y vistas.
Establece la estructura básica de la aplicación y gestiona la navegación global. -->


<template>
  <div class="h-screen flex flex-col">
    <!-- Header -->
    <HeaderView class="h-16 flex-shrink-0" />
    <!-- Main Content -->
    <div class="flex flex-1 overflow-hidden">
      <!-- Sidebar -->
      <LateralHeaderView class="h-full" />
      <!-- Router View -->
      <div class="flex-1 overflow-y-auto">
        <router-view />
      </div>
    </div>
  </div>
</template>

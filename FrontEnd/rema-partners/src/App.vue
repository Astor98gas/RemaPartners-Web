<script lang="ts">
import { defineComponent } from 'vue';
import HeaderView from '@/views/HeaderView.vue';

export default defineComponent({
  name: 'App',
  components: {
    HeaderView
  }
});
</script>

<template>
  <div>
    <HeaderView />
    <router-view></router-view>
  </div>
</template>

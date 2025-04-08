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

<style scoped>
.home-button {
  padding: 10px 20px;
  background-color: #42b983;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  margin-top: 20px;
}

.home-button:hover {
  background-color: #3aa876;
}
</style>

<script setup lang="ts">
import { useRouter } from 'vue-router';

const router = useRouter();

const goHome = () => {
  router.push('/home');
};

const goRoot = () => {
  router.push('/');
};
</script>

<template>
  <div>
    <img @click="goRoot" alt="Vue logo" class="logo" src="@/assets/logo.png" width="125" height="125" />
    <button @click="goHome" class="home-button">Ir a Home</button>
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

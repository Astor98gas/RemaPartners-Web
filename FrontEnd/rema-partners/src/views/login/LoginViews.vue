<template>
    <div class="min-h-screen flex items-center justify-center bg-gray-100 py-12 px-4 sm:px-6 lg:px-8">
        <div class="max-w-md w-full space-y-8 shadow-2xl rounded-3xl bg-white p-6">
            <div class="flex justify-center mb-4">
                <img src="@/assets/logo.png" alt="REMA Partners Logo" class="h-16">
            </div>
            <div>
                <h2 class="mt-6 text-center text-3xl font-extrabold text-gray-900">
                    {{ utf8.t('login.title') }}
                </h2>
            </div>
            <form class="mt-8 space-y-6" @submit.prevent="handleLogin">
                <div class="space-y-4">
                    <InputText v-model="email" type="email" :placeholder="utf8.t('login.email')"
                        :label="utf8.t('login.email')" required />
                    <InputText v-model="password" type="password" :placeholder="utf8.t('login.password')"
                        :label="utf8.t('login.password')" required />
                </div>

                <div class="flex items-center justify-between">
                    <div class="flex items-center">
                        <input id="remember-me" name="remember-me" type="checkbox" v-model="rememberMe"
                            class="h-4 w-4 text-indigo-600 focus:ring-indigo-500 border-gray-300 rounded" />
                        <label for="remember-me" class="ml-2 block text-sm text-gray-900">
                            {{ utf8.t('login.remember') }}
                        </label>
                    </div>

                    <div class="text-sm">
                        <a href="#" class="font-medium text-indigo-600 hover:text-indigo-500">
                            {{ utf8.t('login.forgot') }}
                        </a>
                    </div>
                </div>

                <div>
                    <button type="submit"
                        class="group relative w-full flex justify-center py-2 px-4 border border-transparent text-sm font-medium rounded-md text-white bg-indigo-600 hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500">
                        {{ utf8.t('login.submit') }}
                    </button>
                </div>
            </form>

        </div>
    </div>
</template>

<script lang="ts">
import InputText from '@/components/ui/InputText.vue'
import { useutf8Store } from '@/stores/counter'

export default {
    name: 'LoginView',
    components: {
        InputText
    },
    data() {
        return {
            email: '',
            password: '',
            rememberMe: false,
            utf8: useutf8Store()
        }
    },
    methods: {
        handleLogin() {
            console.log({
                email: this.email,
                password: this.password,
                rememberMe: this.rememberMe
            })
        }
    }
}
</script>
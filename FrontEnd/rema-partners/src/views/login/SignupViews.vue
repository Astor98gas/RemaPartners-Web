<template>
    <div class="min-h-screen flex items-center justify-center bg-gray-100 py-12 px-4 sm:px-6 lg:px-8">
        <div class="max-w-md w-full space-y-8 shadow-2xl rounded-3xl bg-white p-6">
            <div class="flex justify-center mb-4">
                <img src="@/assets/logo.png" alt="REMA Partners Logo" class="h-16">
            </div>
            <div>
                <h2 class="mt-6 text-center text-3xl font-extrabold text-gray-900">
                    {{ utf8.t('signup.title') }}
                </h2>
            </div>
            <form class="mt-8 space-y-6" @submit.prevent="handleLogin">
                <div class="space-y-4">
                    <InputText v-model="email" type="email" :placeholder="utf8.t('signup.email')"
                        :label="utf8.t('signup.email')" required />
                    <InputText v-model="password" type="password" :placeholder="utf8.t('signup.password')"
                        :label="utf8.t('signup.password')" required />
                    <InputText v-model="confirmPassword" type="password" :placeholder="utf8.t('signup.confirmPassword')"
                        :label="utf8.t('signup.confirmPassword')" required />
                </div>

                <div class="flex items-center justify-between">
                    <div class="text-sm">
                        <a href="#" class="font-medium text-indigo-600 hover:text-indigo-500">
                            {{ utf8.t('signup.login') }}
                        </a>
                    </div>
                </div>

                <div class="flex items-center justify-center">
                    <ButtonBasic variant="primary" size="sm" class="text-xl" type="submit">
                        {{ utf8.t('signup.submit') }}
                    </ButtonBasic>
                </div>
            </form>
        </div>
    </div>

    <Modal v-model="showModal" :title="modalTitle" @close="handleClose">
        <p>{{ modalMessage }}</p>
    </Modal>
</template>

<script lang="ts">
import ButtonBasic from '@/components/ui/ButtonBasic.vue'
import InputText from '@/components/ui/InputText.vue'
import { useutf8Store } from '@/stores/counter'
import Modal from '@/components/ui/Modal.vue'

export default {
    name: 'LoginView',
    components: {
        InputText,
        ButtonBasic,
        Modal
    },
    data() {
        return {
            email: '',
            password: '',
            confirmPassword: '',
            rememberMe: false,
            utf8: useutf8Store(),
            showModal: false,
            modalTitle: '',
            modalMessage: '',
        }
    },
    methods: {
        handleLogin() {
            if (this.password !== this.confirmPassword) {
                this.openModal('signup.passwordMismatch')
                return
            }
            if (this.password.length < 6) {
                this.openModal('signup.passwordLength')
                return
            }
            if (!this.email.includes('@')) {
                this.openModal('signup.invalidEmail')
                return
            }
            if (this.email === '' || this.password === '' || this.confirmPassword === '') {
                this.openModal('signup.emptyFields')
                return
            }
            console.log({
                email: this.email,
                password: this.password,
                confirmPassword: this.confirmPassword,
                rememberMe: this.rememberMe
            })
        },
        handleClose() {
            this.showModal = false
        },
        openModal(messageKey: string) {
            this.modalTitle = this.utf8.t('modal.error')
            this.modalMessage = this.utf8.t(messageKey)
            this.showModal = true
        }
    }
}
</script>
import { createRouter, createWebHistory } from 'vue-router'
import { useUsers } from '@/composables/useUsers'

const EmptyComponent = {
    template: '<div></div>'
}

const router = createRouter({
    history: createWebHistory(import.meta.env.BASE_URL),
    routes: [
        {
            path: '/',
            name: 'root',
            component: EmptyComponent
        },
        {
            path: '/login',
            name: 'login',
            component: () => import('../views/login/LoginViews.vue'),
        },
        {
            path: '/signup',
            name: 'signup',
            component: () => import('../views/login/SignupViews.vue'),
        }
    ],
})

router.beforeEach((to, from, next) => {
    // Check login status before each route navigation
    useUsers().isLoggedIn()
        .then(() => {
            // Continue navigation
            next()
        })
        .catch(() => {
            // Still continue navigation, just log the error
            next()
        })
})

export default router

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
        },
        {
            path: '/profile',
            name: 'profile',
            component: () => import('../views/login/ProfileViews.vue'),
        }
    ],
})

router.beforeEach(async (to, from, next) => {
    const usersComposable = useUsers();
    try {
        const userData = await usersComposable.isLoggedIn();
        if (userData) {
            console.log('Usuario autenticado:', userData);
        }
    } catch (error) {
        console.error('Error verificando autenticación:', error);
    }
    next();
})

export default router

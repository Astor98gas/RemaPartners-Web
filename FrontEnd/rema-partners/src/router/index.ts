import { createRouter, createWebHistory } from 'vue-router'

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
            path: '/home',
            name: 'home',
            component: () => import('../views/UsersViews.vue'),
        },
        {
            path: '/login',
            name: 'login',
            component: () => import('../views/LoginViews.vue'),
        }
    ],
})

export default router

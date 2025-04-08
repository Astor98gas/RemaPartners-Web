import { createRouter, createWebHistory } from 'vue-router'
import UsersViews from '@/views/UsersViews.vue'

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
        }
    ],
})

export default router

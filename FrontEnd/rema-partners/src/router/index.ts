import { createRouter, createWebHistory } from 'vue-router'
import { useUsers } from '@/composables/useUsers'

const EmptyComponent = {
    template: '<div></div>'
}

interface RouteMeta {
    requiresAuth?: boolean;
    roles?: string[] | undefined;
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
            meta: { requiresAuth: true, roles: ['USER', 'ADMIN'] }
        },
        {
            path: '/categorias',
            name: 'categorias',
            component: () => import('../views/admin/categorias/CategoriaListView.vue'),
            meta: { requiresAuth: true, roles: ['ADMIN'] }
        },
    ],
})

router.beforeEach(async (to, from, next) => {
    const usersComposable = useUsers();
    try {
        const userData = await usersComposable.isLoggedIn();

        if (to.meta.requiresAuth) {
            if (Array.isArray(to.meta.roles) && !to.meta.roles.includes(userData.rol.name)) {
                return next({ name: 'login' });
            }

            // Modificación clave: Acceder al nombre del rol correctamente
            if (Array.isArray(to.meta.roles) && !to.meta.roles.includes(userData.rol.name)) {
                console.error(`Acceso denegado: El rol ${userData.rol.name} no tiene permisos`);
                return next({ name: 'root' });
            }
        }
        next();
    } catch (error) {
        console.error('Error verificando autenticación:', error);
        next({ name: 'login' });
    }
});

export default router

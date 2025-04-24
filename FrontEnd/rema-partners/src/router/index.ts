import { createRouter, createWebHistory } from 'vue-router'
import { useUsers } from '@/composables/useUsers'
import { useToast } from 'vue-toastification';


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
            meta: { requiresAuth: true, roles: ['COMPRADOR', 'ADMIN', 'VENDEDOR', 'TRABAJADOR'] }
        },
        {
            path: '/admin/categoria/list',
            name: 'categorias',
            component: () => import('../views/admin/categorias/CategoriaListView.vue'),
            meta: { requiresAuth: true, roles: ['ADMIN'] }
        },
        {
            path: '/admin/categoria/create',
            name: 'categoria-create',
            component: () => import('../views/admin/categorias/CategoriaAddView.vue'),
            meta: { requiresAuth: true, roles: ['ADMIN'] }
        },
        {
            path: '/admin/categoria/edit/:id',
            name: 'categoria-edit',
            component: () => import('../views/admin/categorias/CategoriaAddView.vue'),
            meta: { requiresAuth: true, roles: ['ADMIN'] }
        }
    ],
})

router.beforeEach(async (to, from, next) => {
    const usersComposable = useUsers();
    const toast = useToast();
    try {
        if (to.meta.requiresAuth) {
            try {
                const userData = await usersComposable.isLoggedIn();

                if (Array.isArray(to.meta.roles) && !to.meta.roles.includes(userData.rol.name)) {
                    toast.error(`Acceso denegado: El rol ${userData.rol.name} no tiene permisos para acceder a esta página.`);
                    console.error(`Acceso denegado: El rol ${userData.rol.name} no tiene permisos`);
                    return next({ name: 'root' });
                }

                return next();
            } catch (error) {
                toast.warning('Inicia sesión para continuar.');
                console.error('Error verificando autenticación:', error);
                return next({ name: 'root' });
            }
        }

        return next();
    } catch (error) {
        console.error('Error inesperado:', error);
        return next({ name: 'root' });
    }
});

export default router

import { createRouter, createWebHistory } from 'vue-router'
import { useUsers } from '@/composables/useUsers'
import { useToast } from 'vue-toastification';
import { useutf8Store } from '@/stores/counter';


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
            component: () => import('../views/productos/HomeView.vue'),
            meta: { requiresAuth: false }
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
        },
        {
            path: '/producto/create',
            name: 'producto-create',
            component: () => import('../views/productos/ProductosAddView.vue'),
            meta: { requiresAuth: true, roles: ['VENDEDOR', 'ADMIN'] }
        },
        {
            path: '/producto/:id',
            name: 'producto-detail',
            component: () => import('../views/productos/ProductoDetailView.vue'),
            meta: { requiresAuth: false }
        },
        {
            path: '/producto/edit/:id',
            name: 'ProductoEdit',
            component: () => import('@/views/productos/ProductosAddView.vue'),
            meta: { requiresAuth: true }
        },
        {
            path: '/chats',
            name: 'chats-list',
            component: () => import('@/views/chat/ChatsListView.vue'),
            meta: { requiresAuth: true, roles: ['COMPRADOR', 'ADMIN', 'VENDEDOR'] }
        },
        {
            path: '/dashboard',
            name: 'dashboard',
            component: () => import('@/views/dashboard/DashboardView.vue'),
            meta: { requiresAuth: true, roles: ['ADMIN', 'VENDEDOR', 'TRABAJADOR'] }
        },
        {
            path: '/dashboard/producto/:id',
            name: 'producto-estadisticas',
            component: () => import('@/views/dashboard/ProductoEstadisticasView.vue'),
            meta: { requiresAuth: true, roles: ['ADMIN', 'VENDEDOR', 'TRABAJADOR'] }
        },
        {
            path: '/user/:id',
            name: 'public-profile',
            component: () => import('@/views/user/PublicProfileView.vue'),
            meta: { requiresAuth: false }
        },
    ],
})

router.beforeEach(async (to, from, next) => {
    const usersComposable = useUsers();
    const toast = useToast();
    const utf8 = useutf8Store();

    try {
        if (to.meta.requiresAuth) {
            try {
                const userData = await usersComposable.isLoggedIn();

                if (Array.isArray(to.meta.roles) && !to.meta.roles.includes(userData.rol.name)) {
                    const message = utf8.t('auth.access_denied').replace('{role}', utf8.t(`roles.${userData.rol.name}`));
                    toast.error(message);
                    console.error(message);
                    return next({ name: 'root' });
                }

                return next();
            } catch (error) {
                toast.warning(utf8.t('auth.login_required'));
                console.error(utf8.t('auth.unexpected_error'), error);
                return next({ name: 'root' });
            }
        }

        return next();
    } catch (error) {
        console.error(utf8.t('auth.unexpected_error'), error);
        return next({ name: 'root' });
    }
});

export default router

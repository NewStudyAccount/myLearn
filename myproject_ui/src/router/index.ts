import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import Admin from "@/components/admin/Admin.vue";
import ProductList from "@/views/ProductList.vue";
import UserListView from "@/views/users/UserListView.vue";
import RoleListView from "@/views/users/RoleListView.vue";

const router = createRouter({
    history: createWebHistory(import.meta.env.BASE_URL),
    routes: [
        {
            path: '/home',
            name: 'home',
            component: HomeView,
        },
        {
            path: '/about',
            name: 'about',
            // route level code-splitting
            // this generates a separate chunk (About.[hash].js) for this route
            // which is lazy-loaded when the route is visited.
            component: () => import('../views/AboutView.vue'),
        },
        {
            path: '/product',
            name: 'product',
            component: ProductList,
        },
        {
            path: '/admin',
            component: Admin,
            // 使用到 admin.vue 布局的，都需要放置在其子路由下面
            children: [
                {
                    path: "/admin/product",
                    component: ProductList,
                    meta: {
                        title: '产品List测试'
                    }
                },
                {
                    path: "/admin/user",
                    component: UserListView,
                    meta: {
                        title: '用户管理'
                    }
                },
                {
                    path: "/admin/role",
                    component: RoleListView,
                    meta: {
                        title: '角色管理'
                    }
                },
            ]
        }
    ],
})

export default router

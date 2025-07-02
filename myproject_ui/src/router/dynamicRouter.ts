// import { RouteRecordRaw } from 'vue-router';
// import routesData from '@/config/routes.json'; // 引入本地 JSON 文件
//
// // 动态生成路由配置
// export const generateDynamicRoutes = (): RouteRecordRaw[] => {
//   return routesData.map((route: any) => ({
//     path: route.path,
//     name: route.name,
//     component: () => import(`@/views/${route.component}.vue`), // 动态加载组件
//   }));
// };
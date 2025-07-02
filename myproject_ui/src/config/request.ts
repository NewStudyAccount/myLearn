import axios from "axios";

const axiosInstance = axios.create({
    baseURL : "http://localhost:8080",
    timeout: 5000
});


// 添加请求拦截器
axiosInstance.interceptors.request.use(function (config) {
    // 在发送请求之前做些什么
    // 获取当前请求的方法
    const method = config.method?.toUpperCase();

    // 根据不同的请求方法设置不同的请求头
    if (method === 'POST') {
        // 例如：为 POST 请求设置 Content-Type
        config.headers['Content-Type'] = 'application/json;charset=utf-8';
    } else if (method === 'GET') {
        // 例如：为 GET 请求添加 Accept 头
        config.headers['Accept'] = 'application/json';
    }

    // 可以统一添加 token 到 Authorization 头中
    const token = localStorage.getItem('token');
    if (token) {
        config.headers['Authorization'] = `Bearer ${token}`;
    }
    return config;
}, function (error) {
    // 对请求错误做些什么
    console.error('请求异常:', error);
    return Promise.reject(error);
});


// 添加响应拦截器
axiosInstance.interceptors.response.use(
    function (response) {
        const res = response.data;

        console.log('返回值',response)

        if (res.code !== '200') {
            console.error('业务错误:', res.message || '请求失败');
            return Promise.reject(new Error(res.message || 'Error'));
        }

        return res.data;
    },
    function (error) {
        console.error('请求异常:', error);

        if (error.response) {
            switch (error.response.status) {
                case 401:
                    console.warn('未授权，请重新登录');
                    // router.push('/login')
                    break;
                case 403:
                    console.warn('权限不足');
                    break;
                case 404:
                    console.warn('请求地址不存在');
                    break;
                case 500:
                    console.warn('服务器内部错误');
                    break;
                default:
                    console.warn('其他错误');
            }
        } else if (error.request) {
            console.warn('无响应，可能是网络问题');
        } else {
            console.warn('请求配置错误:', error.message);
        }

        return Promise.reject(error);
    }
);



// 响应拦截器
// axiosInstance.interceptors.response.use(
//     response => {
//         return response.data
//     },
//     async error => {
//         const originalRequest = error.config
//         const authStore = useAuthStore()
//
//         // 如果是 401 并且不是刷新请求本身
//         if (error.response?.status === 401 && !originalRequest._retry) {
//             if (authStore.isRefreshing) {
//                 // 已经在刷新中，将请求加入队列
//                 return new Promise((resolve, reject) => {
//                     authStore.failedQueue.push({ resolve, reject })
//                 }).then(token => {
//                     originalRequest.headers['Authorization'] = `Bearer ${token}`
//                     return apiClient(originalRequest)
//                 }).catch(err => {
//                     return Promise.reject(err)
//                 })
//             }
//
//             originalRequest._retry = true
//             authStore.isRefreshing = true
//
//             try {
//                 const newToken = await refreshToken()
//                 authStore.failedQueue.forEach(({ resolve }) => resolve(newToken))
//                 authStore.failedQueue = []
//
//                 // 更新请求头并重新发送
//                 originalRequest.headers['Authorization'] = `Bearer ${newToken}`
//                 return apiClient(originalRequest)
//             } catch (err) {
//                 authStore.failedQueue.forEach(({ reject }) => reject(err))
//                 authStore.failedQueue = []
//                 authStore.clearTokens()
//                 // 跳转登录页（可选）
//                 // window.location.href = '/login'
//                 return Promise.reject(err)
//             } finally {
//                 authStore.isRefreshing = false
//             }
//         }
//
//         return Promise.reject(error)
//     }
// );

export default axiosInstance;
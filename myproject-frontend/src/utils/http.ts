import axios from 'axios'
import type { AxiosInstance, AxiosResponse } from 'axios'
import { ElMessage } from 'element-plus'

// 创建 axios 实例
const http: AxiosInstance = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL || '/project',
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json',
  },
})

// 请求拦截器
http.interceptors.request.use(
    (config) => {
      // 从 localStorage 获取 token
      const token = localStorage.getItem('token')
      // if (token) {
      //   config.headers.Authorization = `Bearer ${token}`
      // }


      // 是否需要携带 token
      const isToken = config.headers?.isToken === false;
      // 是否需要防止数据重复提交
      const isRepeatSubmit = config.headers?.repeatSubmit === false;
      // 是否需要加密
      // const isEncrypt = config.headers?.isEncrypt === 'true';

      //设置token 让每个请求携带自定义token 请根据实际情况自行修改
      if (token && !isToken) {
        config.headers['Authorization'] = 'Bearer ' + token;
      }

      // get请求映射params参数
      if (config.method === 'get' && config.params) {
        let url = config.url + '?' + tansParams(config.params);
        url = url.slice(0, -1);
        config.params = {};
        config.url = url;
      }

      //针对post 和 put请求设置防止重复提交
      if (!isRepeatSubmit && (config.method === 'post' || config.method === 'put')) {
        const requestObj = {
          url: config.url,
          data: typeof config.data === 'object' ? JSON.stringify(config.data) : config.data,
          time: new Date().getTime()
        };
        const sessionObj = JSON.parse(sessionStorage.getItem('sessionObj') || 'null');
        if (sessionObj === undefined || sessionObj === null || sessionObj === '') {
          sessionStorage.setItem('sessionObj', JSON.stringify(requestObj));
        } else {
          const s_url = sessionObj.url; // 请求地址
          const s_data = sessionObj.data; // 请求数据
          const s_time = sessionObj.time; // 请求时间
          const interval = 500; // 间隔时间(ms)，小于此时间视为重复提交
          if (s_data === requestObj.data && requestObj.time - s_time < interval && s_url === requestObj.url) {
            const message = '数据正在处理，请勿重复提交';
            console.warn(`[${s_url}]: ` + message);
            return Promise.reject(new Error(message));
          } else {
            sessionStorage.setItem('sessionObj', JSON.stringify(requestObj));
          }
        }
      }

      // 文件上传特殊处理：移除 Content-Type，让浏览器自动设置 boundary
      if (config.data instanceof FormData) {
        delete config.headers['Content-Type'];
      }

      return config
    },
    (error) => {
      console.error('请求错误:', error)
      return Promise.reject(error)
    }
)

// 响应拦截器
http.interceptors.response.use(
    (response: AxiosResponse) => {

      console.log('响应数据:', response)
      console.log('响应数据:', response.data)

      // 处理文件下载：如果是 blob 类型，直接返回
      if (response.config.responseType === 'blob') {
        return response.data
      }

      return response.data
    },
    (error) => {
      if (error.response) {
        const { status, data } = error.response
        switch (status) {
          case 401:
            ElMessage.error('未授权，请重新登录')
            localStorage.removeItem('token')
            window.location.href = '/login'
            break
          case 403:
            ElMessage.error('拒绝访问')
            break
          case 404:
            ElMessage.error('请求地址不存在')
            break
          case 500:
            ElMessage.error('服务器错误')
            break
          default:
            ElMessage.error(data?.message || '请求失败')
        }
      } else {
        ElMessage.error('网络错误，请检查网络连接')
      }
      return Promise.reject(error)
    }
)



/**
 * 参数处理
 * @param {*} params  参数
 */
export const tansParams = (params: any) => {
  let result = '';
  for (const propName of Object.keys(params)) {
    const value = params[propName];
    const part = encodeURIComponent(propName) + '=';
    if (value !== null && value !== '' && typeof value !== 'undefined') {
      if (typeof value === 'object') {
        for (const key of Object.keys(value)) {
          if (value[key] !== null && value[key] !== '' && typeof value[key] !== 'undefined') {
            const params = propName + '[' + key + ']';
            const subPart = encodeURIComponent(params) + '=';
            result += subPart + encodeURIComponent(value[key]) + '&';
          }
        }
      } else {
        result += part + encodeURIComponent(value) + '&';
      }
    }
  }
  return result;
};

export default http

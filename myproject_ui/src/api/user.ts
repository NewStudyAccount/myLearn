
import type {UserLoginVO, UserQueryVO} from "@/types/type.ts";
import axiosInstance from "@/config/request.ts";

// 登录
export const login = (data: UserLoginVO) => {
    return axiosInstance.post('/project/admin/login', data )
}


export const getUserInfo = (data: UserQueryVO) => {
    return axiosInstance.post( '/project/user/queryUserInfo', data )
}

export const getDynamicRouter = (data: UserQueryVO) => {
    return axiosInstance.post( '/project/user/queryUserDynamicRouter', data )
}
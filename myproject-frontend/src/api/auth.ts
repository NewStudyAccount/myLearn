import http from '@/utils/http'

export interface LoginParams {
  USER_NAME: string
  PASS_WORD: string
}

export interface LoginResult {
  token: string
}

export interface SysUserDto {
  userId: string
  userName: string
  userPwd: string
  userAvatorUrl: string | null
  userSex: string
  userPhone: string | null
}

export interface UserInfoData {
  sysUserDto: SysUserDto
  permissionList: string[]
  enabled: boolean
  password: string
  username: string
  credentialsNonExpired: boolean
  accountNonExpired: boolean
  accountNonLocked: boolean
}

export interface UserInfo {
  permissions: string[]
  roles: string[]
  user: UserInfoData
}

export interface ApiResponse<T> {
  code: string
  msg: string
  data: T
}

export function login2(params: LoginParams): Promise<ApiResponse<LoginResult>> {
  return http.post('/auth/login', params)
}


export function login(params: LoginParams): Promise<ApiResponse<LoginResult>> {

  return http({
    url: '/auth/login',
    headers: {
      isToken: false,
      repeatSubmit: false
    },
    method: 'post',
    data: params
  })

}


export function getUserInfo(): Promise<ApiResponse<UserInfo>> {

  return http({
    url: '/auth/me',
    method: 'post'
  })
}

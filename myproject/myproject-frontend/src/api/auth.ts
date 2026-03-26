export interface LoginParams {
  username: string
  password: string
}

export interface LoginResult {
  token: string
}

export interface UserInfo {
  userId: number
  username: string
  nickname: string
  avatar?: string
}

// 登录接口
export function login(username: string, password: string): Promise<LoginResult> {
  // 模拟登录，实际项目中替换为真实接口
  return new Promise((resolve, reject) => {
    setTimeout(() => {
      if (username === 'admin' && password === '123456') {
        resolve({
          token: 'mock-token-' + Date.now(),
        })
      } else {
        reject(new Error('用户名或密码错误'))
      }
    }, 500)
  })
  // 真实接口调用
  // return http.post('/auth/login', { username, password })
}

// 登出接口
export function logout(): Promise<void> {
  // 模拟登出
  return new Promise((resolve) => {
    setTimeout(() => {
      resolve()
    }, 200)
  })
  // 真实接口调用
  // return http.post('/auth/logout')
}

// 获取用户信息接口
export function getUserInfo(): Promise<UserInfo> {
  // 模拟获取用户信息
  return new Promise((resolve) => {
    setTimeout(() => {
      resolve({
        userId: 1,
        username: 'admin',
        nickname: '管理员',
        avatar: '',
      })
    }, 200)
  })
  // 真实接口调用
  // return http.get('/auth/userinfo')
}

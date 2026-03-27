import http from '@/utils/http'

export interface MenuItem {
  menuId: number
  menuName: string
  perCode: string
  menuType: string
  menuSort: number
  parentId: number
  path: string
  component: string | null
  componentName: string | null
  children?: MenuItem[]
}

export interface ApiResponse<T> {
  code: string
  msg: string
  data: T
}

export function getMenuTree(): Promise<ApiResponse<MenuItem[]>> {
  return http.post('/menu/tree')
}

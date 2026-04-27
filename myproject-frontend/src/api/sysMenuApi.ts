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


export interface SysMenu {
  menuId: number
  menuName: string
  perCode: string
  menuType: string
  menuSort: number
  parentId: number
  path: string
  component: string
  componentName: string
  status: number
}

export interface PageQuery {
  pageNum: number
  pageSize: number
}

export interface SysMenuListParams {
  menuName?: string
  perCode?: string
  menuType?: string
  path?: string
  component?: string
  componentName?: string
  pageQuery: PageQuery
}

export function getMenuTree(): Promise<ApiResponse<MenuItem[]>> {
  return http.post('/sysMenu/tree')
}


export function listSysMenuTree() {
  return http({
    url: '/sysMenu/listTree',
    headers: {
      repeatSubmit: false
    },
    method: 'post',
  })
}


export function listSysMenu(params: SysMenuListParams) {
  return http({
    url: '/sysMenu/list',
    headers: {
      repeatSubmit: false
    },
    method: 'post',
    data: params
  })
}

export function getByIdSysMenu(id: number) {
  return http({
    url: `/sysMenu/${id}`,
    method: 'get'
  })
}

export function createSysMenu(data: Partial<SysMenu>) {
  return http({
    url: '/sysMenu',
    method: 'post',
    data
  })
}

export function updateSysMenu(data: Partial<SysMenu>) {
  return http({
    url: '/sysMenu',
    method: 'put',
    data
  })
}

export function deleteSysMenu(id: number) {
  return http({
    url: `/sysMenu/${id}`,
    method: 'delete'
  })
}

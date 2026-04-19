import http from '@/utils/http'

export interface SysRole {
  roleId?: number
  roleName: string
  menuIds: any[]
}


export interface PageQuery {
  pageNum: number
  pageSize: number
}

export interface SysRoleListParams {
  roleName?: string
  pageQuery: PageQuery
}

export function listSysRole(params: SysRoleListParams) {
  return http({
    url: '/sysRole/list',
    headers: {
      repeatSubmit: false
    },
    method: 'post',
    data: params
  })
}

export function getByIdSysRole(id: number) {
  return http({
    url: `/sysRole/${id}`,
    method: 'get'
  })
}

export function createSysRole(data: Partial<SysRole>) {
  return http({
    url: '/sysRole',
    method: 'post',
    data
  })
}

export function updateSysRole(data: Partial<SysRole>) {
  return http({
    url: '/sysRole/updateRole',
    method: 'post',
    data
  })
}

export function deleteSysRole(id: number) {
  return http({
    url: `/sysRole/${id}`,
    method: 'delete'
  })
}

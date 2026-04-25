import http from '@/utils/http'

export interface SysCategory {
  id: number
  name: string
  isDeleted: string
}

export interface PageQuery {
  pageNum: number
  pageSize: number
}

export interface SysCategoryListParams {
  name?: string
  isDeleted?: string
  pageQuery: PageQuery
}

export function listSysCategory(params: SysCategoryListParams) {
  return http({
    url: '/sysCategory/list',
    headers: {
      repeatSubmit: false
    },
    method: 'post',
    data: params
  })
}

export function getSysCategoryById(id: number) {
  return http({
    url: `/sysCategory/${id}`,
    method: 'get'
  })
}

export function addSysCategory(data: Partial<SysCategory>) {
  return http({
    url: '/sysCategory/add',
    method: 'post',
    data
  })
}

export function updateSysCategory(data: Partial<SysCategory>) {
  return http({
    url: '/sysCategory/update',
    method: 'post',
    data
  })
}

export function deleteSysCategory(id: number) {
  return http({
    url: `/sysCategory/${id}`,
    method: 'delete'
  })
}

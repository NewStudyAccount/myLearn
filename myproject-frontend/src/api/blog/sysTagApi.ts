import http from '@/utils/http'

export interface SysTag {
  id: number
  name: string
  idDeleted: string
}

export interface PageQuery {
  pageNum: number
  pageSize: number
}

export interface SysTagListParams {
  name?: string
  idDeleted?: string
  pageQuery: PageQuery
}

export function listSysTag(params: SysTagListParams) {
  return http({
    url: '/sysTag/list',
    headers: {
      repeatSubmit: false
    },
    method: 'post',
    data: params
  })
}

export function getSysTagById(id: number) {
  return http({
    url: `/sysTag/${id}`,
    method: 'get'
  })
}

export function addSysTag(data: Partial<SysTag>) {
  return http({
    url: '/sysTag/add',
    method: 'post',
    data
  })
}

export function updateSysTag(data: Partial<SysTag>) {
  return http({
    url: '/sysTag/update',
    method: 'post',
    data
  })
}

export function deleteSysTag(id: number) {
  return http({
    url: `/sysTag/${id}`,
    method: 'delete'
  })
}

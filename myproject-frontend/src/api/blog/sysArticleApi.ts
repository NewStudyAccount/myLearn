import http from '@/utils/http'

export interface SysArticle {
  id: string
  title: string
  cover: string
  isDeleted: string
  readNum: number
}

export interface PageQuery {
  pageNum: number
  pageSize: number
}

export interface SysArticleListParams {
  title?: string
  cover?: string
  isDeleted?: string
  pageQuery: PageQuery
}

export function listSysArticle(params: SysArticleListParams) {
  return http({
    url: '/sysArticle/list',
    headers: {
      repeatSubmit: false
    },
    method: 'post',
    data: params
  })
}

export function getSysArticleById(id: string) {
  return http({
    url: `/sysArticle/${id}`,
    method: 'get'
  })
}

export function addSysArticle(data: Partial<SysArticle>) {
  return http({
    url: '/sysArticle/add',
    method: 'post',
    data
  })
}

export function updateSysArticle(data: Partial<SysArticle>) {
  return http({
    url: '/sysArticle/update',
    method: 'post',
    data
  })
}

export function deleteSysArticle(id: string) {
  return http({
    url: `/sysArticle/${id}`,
    method: 'delete'
  })
}

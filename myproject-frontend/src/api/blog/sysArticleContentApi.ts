import http from '@/utils/http'

export interface SysArticleContent {
  id: number
  articleId: number
  content: string
}

export interface PageQuery {
  pageNum: number
  pageSize: number
}

export interface SysArticleContentListParams {
  content?: string
  pageQuery: PageQuery
}

export function listSysArticleContent(params: SysArticleContentListParams) {
  return http({
    url: '/sysArticleContent/list',
    headers: {
      repeatSubmit: false
    },
    method: 'post',
    data: params
  })
}

export function getSysArticleContentById(id: number) {
  return http({
    url: `/sysArticleContent/${id}`,
    method: 'get'
  })
}

export function addSysArticleContent(data: Partial<SysArticleContent>) {
  return http({
    url: '/sysArticleContent/add',
    method: 'post',
    data
  })
}

export function updateSysArticleContent(data: Partial<SysArticleContent>) {
  return http({
    url: '/sysArticleContent/update',
    method: 'post',
    data
  })
}

export function deleteSysArticleContent(id: number) {
  return http({
    url: `/sysArticleContent/${id}`,
    method: 'delete'
  })
}

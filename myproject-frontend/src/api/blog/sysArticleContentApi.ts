import http from '@/utils/http'

export interface SysArticleContent {
  articleId: string
  content: string
}

export interface SysArticleContentHtml {
  articleId: string
  htmlContent: string
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

export function getSysArticleContentByArticleId(id: string) {
  return http({
    url: `/sysArticleContent/queryByArticleId/${id}`,
    method: 'get'
  })
}

export function getSysArticleContentHtmlByArticleId(id: string) {
  return http({
    url: `/sysArticleContent/queryHtmlByArticleId/${id}`,
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


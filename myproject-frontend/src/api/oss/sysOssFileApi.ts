import http from '@/utils/http'

export interface SysOssFile {
  ossId: number
  fileName: string
  originalName: string
  fileSuffix: string
  fileUrl: string
  contentType: string
}

export interface PageQuery {
  pageNum: number
  pageSize: number
}

export interface SysOssFileListParams {
  fileName?: string
  originalName?: string
  fileSuffix?: string
  fileUrl?: string
  contentType?: string
  pageQuery: PageQuery
}

export function listSysOssFile(params: SysOssFileListParams) {
  return http({
    url: '/sysOssFile/list',
    headers: {
      repeatSubmit: false
    },
    method: 'post',
    data: params
  })
}

export function getByIdSysOssFile(id: number) {
  return http({
    url: `/sysOssFile/${id}`,
    method: 'get'
  })
}

export function createSysOssFile(data: Partial<SysOssFile>) {
  return http({
    url: '/sysOssFile/add',
    method: 'post',
    data
  })
}

export function updateSysOssFile(data: Partial<SysOssFile>) {
  return http({
    url: '/sysOssFile/update',
    method: 'post',
    data
  })
}

export function deleteSysOssFile(id: number) {
  return http({
    url: `/sysOssFile/${id}`,
    method: 'delete'
  })
}

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

export function deleteSysOssFile(id: number) {
  return http({
    url: `/sysOssFile/${id}`,
    method: 'delete'
  })
}


export function uploadSysOssFile(file: File) {
  const formData = new FormData()
  formData.append('file', file)
  return http({
    url: '/sysOssFile/upload',
    method: 'post',
    data: formData,
    headers: {
      repeatSubmit: false
    }
  })
}

export function downloadSysOssFile(fileName: string) {
  return http({
    url: `/sysOssFile/download/${fileName}`,
    method: 'get',
    responseType: 'blob'
  })
}

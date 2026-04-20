import http from '@/utils/http'

export interface SysOssConfig {
  id: number
  configName: string
  provider: string
  endpoint: string
  accessKey: string
  secretKey: string
  bucketName: string
  region: string
  extraConfig: string
  isActive: number
  createdAt: string
  updatedAt: string
}

export interface PageQuery {
  pageNum: number
  pageSize: number
}

export interface SysOssConfigListParams {
  configName?: string
  provider?: string
  endpoint?: string
  accessKey?: string
  secretKey?: string
  bucketName?: string
  region?: string
  extraConfig?: string
  isActive?: number
  createdAt?: string
  updatedAt?: string
  pageQuery: PageQuery
}

export function listSysOssConfig(params: SysOssConfigListParams) {
  return http({
    url: '/sysOssConfig/list',
    headers: {
      repeatSubmit: false
    },
    method: 'post',
    data: params
  })
}

export function getByIdSysOssConfig(id: number) {
  return http({
    url: `/sysOssConfig/${id}`,
    method: 'get'
  })
}

export function createSysOssConfig(data: Partial<SysOssConfig>) {
  return http({
    url: '/sysOssConfig/add',
    method: 'post',
    data
  })
}

export function updateSysOssConfig(data: Partial<SysOssConfig>) {
  return http({
    url: '/sysOssConfig/update',
    method: 'post',
    data
  })
}

export function deleteSysOssConfig(id: number) {
  return http({
    url: `/sysOssConfig/${id}`,
    method: 'delete'
  })
}

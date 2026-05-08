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

export interface MigrateResult {
  ossId?: number
  success: boolean
  newOssId?: number
  newUrl?: string
  error?: string
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

// 导出功能
export function exportFile(id: number) {
  return http({
    url: `/sysOssFile/export/${id}`,
    method: 'get',
    responseType: 'blob'
  })
}

export function exportBatch(ossIds: number[]) {
  return http({
    url: '/sysOssFile/export/batch',
    method: 'post',
    data: ossIds,
    responseType: 'blob'
  })
}

export function exportByCondition(params: { suffix?: string; startTime?: string; endTime?: string }) {
  return http({
    url: '/sysOssFile/export/condition',
    method: 'post',
    params
  })
}

// 导入功能
export function importFile(file: File, configName: string) {
  const formData = new FormData()
  formData.append('file', file)
  formData.append('configName', configName)
  return http({
    url: '/sysOssFile/import/file',
    method: 'post',
    data: formData,
    headers: {
      repeatSubmit: false
    }
  })
}

export function importBatch(files: File[], configName: string) {
  const formData = new FormData()
  files.forEach(file => {
    formData.append('files', file)
  })
  formData.append('configName', configName)
  return http({
    url: '/sysOssFile/import/batch',
    method: 'post',
    data: formData,
    headers: {
      repeatSubmit: false
    }
  })
}

// 跨存储迁移
export function migrateFile(ossId: number, sourceConfig: string, targetConfig: string) {
  return http({
    url: '/sysOssFile/migrate/file',
    method: 'post',
    params: { ossId, sourceConfig, targetConfig }
  })
}

export function migrateBatch(ossIds: number[], sourceConfig: string, targetConfig: string) {
  return http({
    url: '/sysOssFile/migrate/batch',
    method: 'post',
    data: ossIds,
    params: { sourceConfig, targetConfig }
  })
}

export function migrateAll(sourceConfig: string, targetConfig: string) {
  return http({
    url: '/sysOssFile/migrate/all',
    method: 'post',
    params: { sourceConfig, targetConfig }
  })
}

export function getMigrateProgress(taskId: number) {
  return http({
    url: `/sysOssFile/migrate/progress/${taskId}`,
    method: 'get'
  })
}

// 迁移任务管理
export interface MigrateTaskProgress {
  taskId: number
  status: string
  totalCount: number
  successCount: number
  failCount: number
  percent: number
  remaining: number
  sourceConfig: string
  targetConfig: string
  createdAt: string
  finishedAt: string
}

export function createMigrateTask(
  sourceConfig: string,
  targetConfig: string,
  ossIds?: number[],
  deleteSource: boolean = false
) {
  return http({
    url: '/oss/migrate/task',
    method: 'post',
    params: { sourceConfig, targetConfig, ossIds, deleteSource }
  })
}

export function getMigrateTaskProgress(taskId: number) {
  return http({
    url: `/oss/migrate/task/${taskId}/progress`,
    method: 'get'
  })
}

export function cancelMigrateTask(taskId: number) {
  return http({
    url: `/oss/migrate/task/${taskId}/cancel`,
    method: 'post'
  })
}

export function resumeMigrateTask(taskId: number) {
  return http({
    url: `/oss/migrate/task/${taskId}/resume`,
    method: 'post'
  })
}

export function listMigrateTasks(status?: string, pageNum: number = 1, pageSize: number = 10) {
  return http({
    url: '/oss/migrate/task/list',
    method: 'get',
    params: { status, pageNum, pageSize }
  })
}
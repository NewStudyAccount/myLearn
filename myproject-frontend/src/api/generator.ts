import http from '@/utils/http'
import axios from 'axios'

export interface GenerateParams {
  tableName: string
  tablePrefix?: string
  entityPrefix?: string
  generateEntity?: boolean
  generateMapper?: boolean
  generateService?: boolean
  generateController?: boolean
  generateFrontend?: boolean
}

export interface GenerateResult {
  entity?: string
  mapperInterface?: string
  mapperXml?: string
  serviceInterface?: string
  serviceImpl?: string
  controller?: string
  frontendApi?: string
  frontendVue?: string
}

export function getTables(): Promise<any> {
  return http.get('/generator/tables')
}

export function getTableInfo(tableName: string): Promise<any> {
  return http.get(`/generator/table/${tableName}`)
}

export function generateCode(params: GenerateParams): Promise<any> {
  return http.post('/generator/generate', params)
}

export async function downloadCode(params: GenerateParams): Promise<void> {
  const response = await axios({
    url: `${import.meta.env.VITE_API_BASE_URL || '/project'}/generator/download`,
    method: 'POST',
    data: params,
    responseType: 'blob',
    headers: {
      'Content-Type': 'application/json',
      Authorization: `Bearer ${localStorage.getItem('token')}`,
    },
  })

  const blob = new Blob([response.data], { type: 'application/zip' })
  const url = window.URL.createObjectURL(blob)
  const link = document.createElement('a')
  link.href = url
  
  let fileName = params.tableName
  if (params.entityPrefix) {
    fileName = params.entityPrefix + fileName
  }
  fileName = fileName + '_code.zip'
  
  link.setAttribute('download', fileName)
  document.body.appendChild(link)
  link.click()
  document.body.removeChild(link)
  window.URL.revokeObjectURL(url)
}
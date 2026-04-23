import { ElMessage } from 'element-plus'

/**
 * 下载文件
 * @param blob 文件二进制数据
 * @param fileName 文件名
 */
export function downloadFile(blob: Blob, fileName: string) {
    try {
        const url = window.URL.createObjectURL(blob)
        const link = document.createElement('a')
        link.href = url
        link.setAttribute('download', fileName)
        document.body.appendChild(link)
        link.click()
        document.body.removeChild(link)
        window.URL.revokeObjectURL(url)
        ElMessage.success('下载成功')
    } catch (error) {
        console.error('下载失败:', error)
        ElMessage.error('下载失败')
    }
}

/**
 * 从响应头中获取文件名
 * @param response axios 响应对象
 * @returns 文件名
 */
export function getFileNameFromResponse(response: any): string {
    const contentDisposition = response.headers?.['content-disposition']
    if (contentDisposition) {
        const fileNameMatch = contentDisposition.match(/filename[^;=\n]*=((['"]).*?\2|[^;\n]*)/)
        if (fileNameMatch && fileNameMatch[1]) {
            return decodeURIComponent(fileNameMatch[1].replace(/['"]/g, ''))
        }
    }
    return 'download-file'
}

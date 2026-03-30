import http from '@/utils/http';
import type {AxiosPromise} from "axios";





export interface SysUser {
  /** 用户id */
userId: string
  /** 用户名 */
userName: string
  /** 密码 */
userPwd: string
  /** 头像 */
userAvatorUrl?: string
  /** 性别 */
userSex?: string
  /** 手机 */
userPhone?: string
  /** 创建人id */
createId?: string
  /** 创建时间 */
createDate?: any
  /** 修改人id */
updateId?: string
  /** 修改时间 */
updateDate?: any
  /** 逻辑删除0：有效，1删除 */
isDeleted?: string
}

export function getListSysUser(query?: any): AxiosPromise<any> {
  return http({
    url: '/system/user/list',
    method: 'get',
    params: query
  });
}

export function getByIdSysUser(userId: string): AxiosPromise<SysUser> {
  return http({
    url: `/system/user/${userId}`,
    method: 'get'
  });
}

export function createSysUser(data: SysUser): AxiosPromise<void> {
  return http({
    url: '/system/user',
    method: 'post',
    data
  });
}

export function updateSysUser(data: SysUser): AxiosPromise<void> {
  return http({
    url: '/system/user',
    method: 'put',
    data
  });
}

export function deleteSysUser(userId: string): AxiosPromise<void> {
  return http({
    url: `/system/user/${userId}`,
    method: 'delete'
  });
}

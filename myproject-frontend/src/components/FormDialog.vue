<template>
  <el-dialog v-model="visible" :title="title" width="800px" destroy-on-close>
    <el-form ref="formRef" :model="form" :rules="rules" label-width="120px">
      <el-form-item label="用户名" prop="userName">
        <el-input v-model="form.userName" placeholder="请输入用户名" />
      </el-form-item>
      <el-form-item label="密码" prop="userPwd">
        <el-input v-model="form.userPwd" placeholder="请输入密码" />
      </el-form-item>
      <el-form-item label="头像" prop="userAvatorUrl">
        <el-input v-model="form.userAvatorUrl" placeholder="请输入头像" />
      </el-form-item>
      <el-form-item label="性别" prop="userSex">
        <el-input v-model="form.userSex" placeholder="请输入性别" />
      </el-form-item>
      <el-form-item label="手机" prop="userPhone">
        <el-input v-model="form.userPhone" placeholder="请输入手机" />
      </el-form-item>
      <el-form-item label="创建人id" prop="createId">
        <el-input v-model="form.createId" placeholder="请输入创建人id" />
      </el-form-item>
      <el-form-item label="创建时间" prop="createDate">
        <el-date-picker v-model="form.createDate" type="datetime" placeholder="请选择创建时间" />
      </el-form-item>
      <el-form-item label="修改人id" prop="updateId">
        <el-input v-model="form.updateId" placeholder="请输入修改人id" />
      </el-form-item>
      <el-form-item label="修改时间" prop="updateDate">
        <el-date-picker v-model="form.updateDate" type="datetime" placeholder="请选择修改时间" />
      </el-form-item>
      <el-form-item label="逻辑删除0：有效，1删除" prop="isDeleted">
        <el-input v-model="form.isDeleted" placeholder="请输入逻辑删除0：有效，1删除" />
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="visible = false">取消</el-button>
      <el-button type="primary" @click="handleSubmit">确定</el-button>
    </template>
  </el-dialog>
</template>

<script setup lang="ts">
import { ref, reactive, watch } from 'vue'
import { ElMessage } from 'element-plus'
import { createSysUser, updateSysUser , type SysUser} from '@/api/sysUserApi.ts'

const props = defineProps<{
  visible: boolean
  title: string
  data?: SysUser
}>()

const emit = defineEmits<{
  'update:visible': [value: boolean]
  success: []
}>()

const visible = ref(props.visible)
const formRef = ref()

watch(() => props.visible, (val) => {
  visible.value = val
})

watch(visible, (val) => {
  emit('update:visible', val)
})

watch(() => props.data, (val) => {
  if (val) {
    Object.assign(form, val)
  } else {
    Object.keys(form).forEach(key => {
      (form as any)[key] = undefined
    })
  }
}, { immediate: true })

const form = reactive<Partial<SysUser>>({})
const rules = reactive<Record<string, any[]>>({})

const handleSubmit = async () => {
  const valid = await formRef.value?.validate()
  if (!valid) return

  try {
    const data = { ...form }
    if (data.userId) {
      await updateSysUser(data)
      ElMessage.success('修改成功')
    } else {
      await createSysUser(data)
      ElMessage.success('新增成功')
    }
    visible.value = false
    emit('success')
  } catch {}
}
</script>

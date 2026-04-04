<template>
  <el-dialog v-model="visible" :title="title" width="800px" destroy-on-close>
    <el-form ref="formRef" :model="form" :rules="rules" label-width="120px">
      <el-form-item label="角色名" prop="roleName">
        <el-input v-model="form.roleName" placeholder="请输入角色名" />
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
import { createSysRole, updateSysRole } from '@/api/sysRoleApi'
import type { SysRole } from '@/api/sysRoleApi'

const props = defineProps<{
  visible: boolean
  title: string
  data?: SysRole
}>()

const emit = defineEmits<{
  'update:visible': [value: boolean]
  success: []
}>()

const visible = ref(props.visible)
const formRef = ref()

const form = reactive<Partial<SysRole>>({})
const rules = reactive<Record<string, any[]>>({})

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


const handleSubmit = async () => {
  const valid = await formRef.value?.validate()
  if (!valid) return

  try {
    const data = { ...form }
    if (data.roleId) {
      await updateSysRole(data)
      ElMessage.success('修改成功')
    } else {
      await createSysRole(data)
      ElMessage.success('新增成功')
    }
    visible.value = false
    emit('success')
  } catch {}
}
</script>

<template>
  <el-dialog v-model="visible" :title="title" width="800px" destroy-on-close>
    <el-form ref="formRef" :model="form" :rules="rules" label-width="120px">
      <el-form-item label="菜单名称" prop="menuName">
        <el-input v-model="form.menuName" placeholder="请输入菜单名称" />
      </el-form-item>
      <el-form-item label="权限code" prop="perCode">
        <el-input v-model="form.perCode" placeholder="请输入权限code" />
      </el-form-item>
      <el-form-item label="菜单类型" prop="menuType">
        <el-input v-model="form.menuType" placeholder="请输入菜单类型" />
      </el-form-item>
      <el-form-item label="排序" prop="menuSort">
        <el-input v-model="form.menuSort" placeholder="请输入排序" />
      </el-form-item>
      <el-form-item label="父级id" prop="parentId">
        <el-input v-model="form.parentId" placeholder="请输入父级id" />
      </el-form-item>
      <el-form-item label="路由地址" prop="path">
        <el-input v-model="form.path" placeholder="请输入路由地址" />
      </el-form-item>
      <el-form-item label="组件路径" prop="component">
        <el-input v-model="form.component" placeholder="请输入组件路径" />
      </el-form-item>
      <el-form-item label="" prop="componentName">
        <el-input v-model="form.componentName" placeholder="请输入" />
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
import { createSysMenu, updateSysMenu } from '@/api/sysMenuApi.ts'
import type { SysMenu } from '@/api/sysMenuApi.ts'

const props = defineProps<{
  visible: boolean
  title: string
  data?: SysMenu
}>()

const emit = defineEmits<{
  'update:visible': [value: boolean]
  success: []
}>()

const visible = ref(props.visible)
const formRef = ref()

const form = reactive<Partial<SysMenu>>({})
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
    if (data.menuId) {
      await updateSysMenu(data)
      ElMessage.success('修改成功')
    } else {
      await createSysMenu(data)
      ElMessage.success('新增成功')
    }
    visible.value = false
    emit('success')
  } catch {}
}
</script>

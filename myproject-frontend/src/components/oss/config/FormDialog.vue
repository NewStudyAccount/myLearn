<template>
  <el-dialog v-model="visible" :title="title" width="800px" destroy-on-close>
    <el-form ref="formRef" :model="form" :rules="rules" label-width="120px">
      <el-form-item label="配置名称（唯一标识）" prop="configName">
        <el-input v-model="form.configName" placeholder="请输入配置名称（唯一标识）" />
      </el-form-item>
      <el-form-item label="提供商类型（aliyun、minio等）" prop="provider">
        <el-input v-model="form.provider" placeholder="请输入提供商类型（aliyun、minio等）" />
      </el-form-item>
      <el-form-item label="服务端点" prop="endpoint">
        <el-input v-model="form.endpoint" placeholder="请输入服务端点" />
      </el-form-item>
      <el-form-item label="访问密钥" prop="accessKey">
        <el-input v-model="form.accessKey" placeholder="请输入访问密钥" />
      </el-form-item>
      <el-form-item label="秘密密钥" prop="secretKey">
        <el-input v-model="form.secretKey" placeholder="请输入秘密密钥" />
      </el-form-item>
      <el-form-item label="存储桶名称" prop="bucketName">
        <el-input v-model="form.bucketName" placeholder="请输入存储桶名称" />
      </el-form-item>
      <el-form-item label="区域（可选）" prop="region">
        <el-input v-model="form.region" placeholder="请输入区域（可选）" />
      </el-form-item>
      <el-form-item label="JSON格式的扩展配置" prop="extraConfig">
        <el-input v-model="form.extraConfig" type="textarea" placeholder="请输入JSON格式的扩展配置" />
      </el-form-item>
      <el-form-item label="是否启用（1启用，0禁用）" prop="isActive">
        <el-input v-model="form.isActive" placeholder="请输入是否启用（1启用，0禁用）" />
      </el-form-item>
      <el-form-item label="创建时间" prop="createdAt">
        <el-date-picker v-model="form.createdAt" type="datetime" placeholder="请选择创建时间" />
      </el-form-item>
      <el-form-item label="更新时间" prop="updatedAt">
        <el-date-picker v-model="form.updatedAt" type="datetime" placeholder="请选择更新时间" />
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
import { createSysOssConfig, updateSysOssConfig } from '@/api/sysOssConfigApi'
import type { SysOssConfig } from '@/api/sysOssConfigApi'

const props = defineProps<{
  visible: boolean
  title: string
  data?: SysOssConfig
}>()

const emit = defineEmits<{
  'update:visible': [value: boolean]
  success: []
}>()

const visible = ref(props.visible)
const formRef = ref()
const form = reactive<Partial<SysOssConfig>>({})
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
    if (data.id) {
      await updateSysOssConfig(data)
      ElMessage.success('修改成功')
    } else {
      await createSysOssConfig(data)
      ElMessage.success('新增成功')
    }
    visible.value = false
    emit('success')
  } catch {}
}
</script>

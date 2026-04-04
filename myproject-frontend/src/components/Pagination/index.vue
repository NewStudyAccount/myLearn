<template>
  <div class="pagination-wrapper">
    <el-pagination
      v-model:current-page="innerCurrentPage"
      v-model:page-size="innerPageSize"
      :total="total"
      :page-sizes="pageSizes"
      :layout="layout"
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
      @prev-click="handlePrevClick"
      @next-click="handleNextClick"
    />
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue'

interface PaginationProps {
  currentPage?: number
  pageSize?: number
  total: number
  pageSizes?: number[]
  layout?: string
}

const props = withDefaults(defineProps<PaginationProps>(), {
  currentPage: 1,
  pageSize: 10,
  pageSizes: () => [10, 20, 50, 100],
  layout: 'total, sizes, prev, pager, next, jumper'
})

const emit = defineEmits<{
  'update:currentPage': [value: number]
  'update:pageSize': [value: number]
  'size-change': [value: number]
  'current-change': [value: number]
  'prev-click': [value: number]
  'next-click': [value: number]
}>()

const innerCurrentPage = computed({
  get: () => props.currentPage,
  set: (val: number) => emit('update:currentPage', val)
})

const innerPageSize = computed({
  get: () => props.pageSize,
  set: (val: number) => emit('update:pageSize', val)
})

const handleSizeChange = (val: number) => {
  emit('size-change', val)
}

const handleCurrentChange = (val: number) => {
  emit('current-change', val)
}

const handlePrevClick = (val: number) => {
  emit('prev-click', val)
}

const handleNextClick = (val: number) => {
  emit('next-click', val)
}
</script>

<style scoped>
.pagination-wrapper {
  display: flex;
  justify-content: flex-end;
  padding: 16px 0;
  margin-top: 16px;
}

.pagination-wrapper :deep(.el-pagination) {
  font-weight: normal;
}

.pagination-wrapper :deep(.el-pagination__total) {
  margin-right: 16px;
}

.pagination-wrapper :deep(.el-pagination__sizes) {
  margin-right: 16px;
}
</style>

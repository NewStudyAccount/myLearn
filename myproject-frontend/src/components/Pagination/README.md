# Pagination 分页组件

封装 Element Plus 的 `ElPagination` 组件，提供统一的 API 和样式。

## 基本用法

```vue
<template>
  <Pagination
    v-model:current-page="currentPage"
    v-model:page-size="pageSize"
    :total="total"
    @current-change="handlePageChange"
    @size-change="handleSizeChange"
  />
</template>

<script setup lang="ts">
import { ref } from 'vue'
import Pagination from '@/components/Pagination/index.vue'

const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(100)

const handlePageChange = (page: number) => {
  console.log('当前页:', page)
}

const handleSizeChange = (size: number) => {
  console.log('每页条数:', size)
}
</script>
```

## Props

| 参数 | 说明 | 类型 | 默认值 |
|------|------|------|--------|
| currentPage | 当前页码 | `number` | `1` |
| pageSize | 每页条数 | `number` | `10` |
| total | 数据总数 | `number` | **必填** |
| pageSizes | 每页条数选择器选项 | `number[]` | `[10, 20, 50, 100]` |
| layout | 分页布局 | `string` | `'total, sizes, prev, pager, next, jumper'` |

## Events

| 事件名 | 说明 | 回调参数 |
|--------|------|----------|
| update:currentPage | currentPage 更新时触发 | `(value: number)` |
| update:pageSize | pageSize 更新时触发 | `(value: number)` |
| size-change | pageSize 改变时触发 | `(value: number)` |
| current-change | currentPage 改变时触发 | `(value: number)` |
| prev-click | 上一页按钮点击时触发 | `(value: number)` |
| next-click | 下一页按钮点击时触发 | `(value: number)` |

## 自定义布局

```vue
<Pagination
  v-model:current-page="currentPage"
  :total="100"
  layout="total, prev, pager, next"
/>
```

## 与表格配合使用

```vue
<template>
  <div>
    <el-table :data="tableData">
      <el-table-column prop="id" label="ID" />
      <el-table-column prop="name" label="名称" />
    </el-table>
    
    <Pagination
      v-model:current-page="currentPage"
      v-model:page-size="pageSize"
      :total="total"
      @current-change="fetchData"
    />
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import Pagination from '@/components/Pagination/index.vue'

const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)
const tableData = ref([])

const fetchData = async () => {
  // 根据 currentPage 和 pageSize 请求数据
}

onMounted(() => {
  fetchData()
})
</script>
```

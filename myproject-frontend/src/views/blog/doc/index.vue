<template>
  <div class="doc-display-container">
    <div class="doc-header">
      <el-button @click="goBack" :icon="ArrowLeft">返回文章列表</el-button>
      <h2 class="doc-title">{{ articleTitle }}</h2>
    </div>

    <div v-if="loading" class="doc-loading">
      <el-skeleton :rows="10" animated />
    </div>

    <div v-else-if="error" class="doc-error">
      <el-result icon="error" :title="error" sub-title="请稍后重试或返回文章列表">
        <template #extra>
          <el-button type="primary" @click="goBack">返回文章列表</el-button>
          <el-button @click="fetchHtmlContent">重新加载</el-button>
        </template>
      </el-result>
    </div>

    <div v-else class="doc-content-wrapper">
      <div class="doc-content markdown-body" v-html="htmlContent"></div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ArrowLeft } from '@element-plus/icons-vue'
import { getSysArticleContentHtmlByArticleId } from '@/api/blog/sysArticleContentApi'
import { getSysArticleById } from '@/api/blog/sysArticleApi'

const route = useRoute()
const router = useRouter()

// ❌ 修改前
// const articleId = Number(route.params.articleId)

// ✅ 修改后
const articleId = route.params.articleId as string
const articleTitle = ref('')
const htmlContent = ref('')
const loading = ref(true)
const error = ref('')

const fetchHtmlContent = async () => {
  loading.value = true
  error.value = ''
  try {
    const res = await getSysArticleContentHtmlByArticleId(articleId)
    htmlContent.value = res.data?.htmlContent || ''
  } catch (e: any) {
    error.value = e?.message || '加载文档内容失败'
  } finally {
    loading.value = false
  }
}

const fetchArticleTitle = async () => {
  try {
    const res = await getSysArticleById(articleId)
    articleTitle.value = res.data?.title || '文档展示'
  } catch {
    articleTitle.value = '文档展示'
  }
}

const goBack = () => {
  router.push('/blog/article')
}

onMounted(() => {
  fetchArticleTitle()
  fetchHtmlContent()
})
</script>

<style scoped lang="scss">
.doc-display-container {
  padding: 20px;
  max-width: 960px;
  margin: 0 auto;
}

.doc-header {
  display: flex;
  align-items: center;
  margin-bottom: 24px;
  gap: 16px;

  .doc-title {
    margin: 0;
    font-size: 22px;
    font-weight: 600;
    color: #303133;
  }
}

.doc-loading {
  padding: 40px 0;
}

.doc-error {
  padding: 60px 0;
}

.doc-content-wrapper {
  background: #fff;
  border-radius: 8px;
  padding: 32px 40px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.06);
}

.markdown-body {
  font-size: 15px;
  line-height: 1.8;
  color: #333;
  word-wrap: break-word;

  :deep(h1) {
    font-size: 28px;
    font-weight: 700;
    margin: 24px 0 16px;
    padding-bottom: 8px;
    border-bottom: 2px solid #eaecef;
  }

  :deep(h2) {
    font-size: 24px;
    font-weight: 600;
    margin: 20px 0 14px;
    padding-bottom: 6px;
    border-bottom: 1px solid #eaecef;
  }

  :deep(h3) {
    font-size: 20px;
    font-weight: 600;
    margin: 18px 0 12px;
  }

  :deep(h4) {
    font-size: 17px;
    font-weight: 600;
    margin: 16px 0 10px;
  }

  :deep(h5),
  :deep(h6) {
    font-size: 15px;
    font-weight: 600;
    margin: 14px 0 8px;
  }

  :deep(p) {
    margin: 0 0 14px;
  }

  :deep(a) {
    color: #409eff;
    text-decoration: none;

    &:hover {
      text-decoration: underline;
    }
  }

  :deep(strong) {
    font-weight: 600;
  }

  :deep(blockquote) {
    margin: 0 0 14px;
    padding: 8px 16px;
    border-left: 4px solid #ddd;
    background: #f9f9f9;
    color: #666;

    p {
      margin: 0;
    }
  }

  :deep(ul),
  :deep(ol) {
    margin: 0 0 14px;
    padding-left: 28px;
  }

  :deep(li) {
    margin: 4px 0;
  }

  :deep(code) {
    padding: 2px 6px;
    font-size: 13px;
    background: #f0f0f0;
    border-radius: 4px;
    font-family: 'SFMono-Regular', Consolas, 'Liberation Mono', Menlo, monospace;
  }

  :deep(pre) {
    margin: 0 0 14px;
    padding: 16px;
    overflow: auto;
    font-size: 13px;
    line-height: 1.5;
    background: #f6f8fa;
    border-radius: 6px;

    code {
      padding: 0;
      background: none;
      font-size: inherit;
    }
  }

  :deep(table) {
    width: 100%;
    margin: 0 0 14px;
    border-collapse: collapse;
    border-spacing: 0;

    th,
    td {
      padding: 8px 12px;
      border: 1px solid #dfe2e5;
      text-align: left;
    }

    th {
      background: #f6f8fa;
      font-weight: 600;
    }

    tr:nth-child(even) {
      background: #fafafa;
    }
  }

  :deep(hr) {
    margin: 20px 0;
    border: none;
    border-top: 1px solid #eaecef;
  }

  :deep(img) {
    max-width: 100%;
    height: auto;
    border-radius: 4px;
  }
}
</style>

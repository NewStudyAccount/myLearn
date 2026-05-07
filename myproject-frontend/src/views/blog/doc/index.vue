<template>
  <div class="doc-standalone-page">
    <header class="doc-header">
      <el-button @click="goBack" :icon="ArrowLeft">返回文章列表</el-button>
      <h1 class="doc-title">{{ articleTitle }}</h1>
    </header>

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

    <div v-else class="doc-body">
      <div class="doc-content-area">
        <div class="doc-content markdown-body" v-html="htmlContent"></div>
      </div>
      <aside v-if="tocItems.length" class="doc-toc">
        <div class="toc-title">目录</div>
        <nav class="toc-list">
          <a
            v-for="item in tocItems"
            :key="item.id"
            :class="['toc-item', { active: activeTocId === item.id }]"
            :style="{ paddingLeft: 12 + (item.level - 1) * 16 + 'px' }"
            @click.prevent="scrollToHeading(item.id)"
          >
            {{ item.text }}
          </a>
        </nav>
      </aside>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, nextTick, onMounted, onUnmounted, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ArrowLeft } from '@element-plus/icons-vue'
import { getSysArticleContentHtmlByArticleId } from '@/api/blog/sysArticleContentApi'
import { getSysArticleById } from '@/api/blog/sysArticleApi'

interface TocItem {
  id: string
  text: string
  level: number
}

const route = useRoute()
const router = useRouter()

const articleId = route.params.articleId as string
const articleTitle = ref('')
const htmlContent = ref('')
const loading = ref(true)
const error = ref('')
const tocItems = ref<TocItem[]>([])
const activeTocId = ref('')

let observer: IntersectionObserver | null = null

const generateToc = () => {
  const container = document.querySelector('.markdown-body')
  if (!container) return

  const headings = container.querySelectorAll('h1, h2, h3, h4, h5, h6')
  const items: TocItem[] = []

  headings.forEach((heading, index) => {
    const id = `heading-${index}`
    heading.id = id
    items.push({
      id,
      text: heading.textContent || '',
      level: parseInt(heading.tagName[1], 10),
    })
  })

  tocItems.value = items
  if (items.length > 0) {
    activeTocId.value = items[0].id
  }
}

const setupObserver = () => {
  if (observer) observer.disconnect()

  observer = new IntersectionObserver(
    (entries) => {
      for (const entry of entries) {
        if (entry.isIntersecting) {
          activeTocId.value = entry.target.id
        }
      }
    },
    {
      rootMargin: '-80px 0px -60% 0px',
      threshold: 0,
    }
  )

  const container = document.querySelector('.markdown-body')
  if (!container) return

  const headings = container.querySelectorAll('h1, h2, h3, h4, h5, h6')
  headings.forEach((heading) => {
    if (heading.id) observer!.observe(heading)
  })
}

const scrollToHeading = (id: string) => {
  const el = document.getElementById(id)
  if (el) {
    el.scrollIntoView({ behavior: 'smooth', block: 'start' })
  }
}

const fetchHtmlContent = async () => {
  loading.value = true
  error.value = ''
  try {
    const res = await getSysArticleContentHtmlByArticleId(articleId)
    htmlContent.value = res.data?.htmlContent || ''
    await nextTick()
    generateToc()
    setupObserver()
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

onUnmounted(() => {
  if (observer) {
    observer.disconnect()
    observer = null
  }
})
</script>

<style scoped lang="scss">
.doc-standalone-page {
  min-height: 100vh;
  background: #f5f7f9;
}

.doc-header {
  position: sticky;
  top: 0;
  z-index: 100;
  display: flex;
  align-items: center;
  gap: 16px;
  padding: 12px 24px;
  background: #fff;
  border-bottom: 1px solid #e6e6e6;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.04);

  .doc-title {
    margin: 0;
    font-size: 20px;
    font-weight: 600;
    color: #303133;
  }
}

.doc-loading {
  padding: 60px 24px;
  max-width: 800px;
  margin: 0 auto;
}

.doc-error {
  padding: 80px 24px;
  max-width: 800px;
  margin: 0 auto;
}

.doc-body {
  display: flex;
  max-width: 1200px;
  margin: 24px auto;
  padding: 0 24px;
  gap: 32px;
}

.doc-content-area {
  flex: 1;
  min-width: 0;
}

.doc-content {
  background: #fff;
  border-radius: 8px;
  padding: 32px 40px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.06);
}

.doc-toc {
  width: 240px;
  flex-shrink: 0;
  position: sticky;
  top: 72px;
  align-self: flex-start;
  max-height: calc(100vh - 96px);
  overflow-y: auto;
  background: #fff;
  border-radius: 8px;
  padding: 16px 0;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.06);

  .toc-title {
    padding: 0 16px 12px;
    font-size: 14px;
    font-weight: 600;
    color: #303133;
    border-bottom: 1px solid #f0f0f0;
    margin-bottom: 8px;
  }

  .toc-list {
    display: flex;
    flex-direction: column;
  }

  .toc-item {
    display: block;
    padding: 6px 16px;
    font-size: 13px;
    line-height: 1.5;
    color: #666;
    text-decoration: none;
    cursor: pointer;
    border-left: 2px solid transparent;
    transition: color 0.2s, border-color 0.2s, background 0.2s;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;

    &:hover {
      color: #409eff;
      background: #f5f7fa;
    }

    &.active {
      color: #409eff;
      border-left-color: #409eff;
      background: #ecf5ff;
      font-weight: 500;
    }
  }
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
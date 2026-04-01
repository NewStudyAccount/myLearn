## 1. 更新 controller.vm

- [x] 1.1 修改分页查询方法：`@GetMapping` → `@PostMapping("/list")`
- [x] 1.2 添加请求体参数：`@RequestBody ${className}QueryPageReq pageReq`
- [x] 1.3 修改返回类型：`Page<${className}>` → `TableDataInfo<${className}>`
- [x] 1.4 调用自定义分页方法：`page()` → `query${className}ListPage(pageReq)`

## 2. 更新 list.vue.vm

- [x] 2.1 替换分页组件：`<pagination>` → `<Pagination>` 并调整属性
- [x] 2.2 修改分页参数结构：`queryParams.pageNo` → `queryParams.pageQuery.pageNum`
- [x] 2.3 添加 `handlePageChange` 和 `handleSizeChange` 方法
- [x] 2.4 修改响应数据处理：`res.list` → `res.data.rows`，`res.total` → `res.data.total`

## 3. 更新 api.ts.vm

- [x] 3.1 修改列表查询方法：`method: 'get'` → `method: 'post'`
- [x] 3.2 修改请求参数：`params` → `data: params`
- [x] 3.3 添加 `PageQuery` 接口定义
- [x] 3.4 修改列表参数接口：添加嵌套 `pageQuery: PageQuery`

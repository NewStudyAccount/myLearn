<script setup lang="ts">

import NavigateLeft from "@/components/admin/components/NavigateLeft.vue";
</script>

<template>
  <div class="common-layout">
    <el-container>
      <el-header>Header</el-header>
      <el-container>

        <el-aside width="200px">
          <NavigateLeft/>
        </el-aside>
        <el-container>

          <!--主体内容-->
          <el-main>
            <router-view/>
          </el-main>

          <el-footer>Footer</el-footer>
        </el-container>
      </el-container>
    </el-container>
  </div>
</template>

<style scoped>

</style>
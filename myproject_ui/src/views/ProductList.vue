<script setup lang="ts">


  import { useShopStore } from '../stores/shop.ts'
  import {ref} from "vue";


  const shopStore = useShopStore();


  // 用于测试的产品列表
  const products = ref([
    { id: 1, name: '苹果', price: 5 },
    { id: 2, name: '香蕉', price: 3 },
    { id: 3, name: '橙子', price: 4 },
    { id: 4, name: '葡萄', price: 10 },
    { id: 5, name: '西瓜', price: 8 }
  ])


</script>

<template>

<div>
  <div>
    <h2>商品列表</h2>
    <ul>
      <li v-for="product in products" :key="product.id">
        {{ product.name }} - ￥{{ product.price }}
        <button @click="shopStore.addShop(product)">加入购物车</button>
      </li>
    </ul>
  </div>

  <div>
    <h1>用户购物车产品总价：{{ shopStore.getShopPrice }}</h1>
    <h1>用户购物车产品足量：{{ shopStore.getShopNums }}</h1>
    <h1>用户购物车产品足量getShopNums2：{{ shopStore.getShopNums2 }}</h1>
  </div>
</div>

</template>

<style scoped>ul {
  list-style-type: none;
  padding-left: 0;
}
li {
  margin: 10px 0;
}
button {
  margin-left: 10px;
}
</style>
import { defineStore } from 'pinia'

//产品信息
interface Product {
  id: number
  name: string
  price: number
}

//购物车产品信息
interface ShopProduct {
  products: Product
  nums: number
  totalPrice: number
}

//购物车信息
interface ShopState {
    shop: ShopProduct[]
    shopPrice: number
    totalNums: number
}



export const useShopStore = defineStore('shop', {
    state: (): ShopState => ({
      shop: [],
      shopPrice: 0,
      totalNums: 0
    }),
    getters: {
    getShopPrice:  (state) => {
      return state.shopPrice
    },
    getShopNums: (state) => {
      return state.shop.reduce((sum, item) => sum + item.nums, 0)
    },
      //购物车数量 ,直接取购物车数量，该数据取值需要每次新增操作，修改操作时进行更新。否则使用上面的方法，获取时每次自动计算，同时上面方法的数据是按照产品数据变化时变化的。符合相应式
    getShopNums2:  (state) => {
      return state.totalNums
    },
    },
    actions: {
    async addShop(product: Product) {
        const existsItem = this.shop.find(item => item.products.id === product.id);
        if (existsItem) {
            existsItem.nums++;
            existsItem.totalPrice = existsItem.nums * existsItem.products.price;
        } else {
            this.shop.push({
                products: product,
                nums: 1,
                totalPrice: product.price
            });
        }
        //购物车总价和数量
        this.shopPrice = this.shopPrice + product.price
        // this.totalNums = this.totalNums + 1
    }
    },
    //默认方式
    // persist: true //启用持久化

    //指定key、存储方式
    // persist: {
    //     storage: sessionStorage, // 使用 session 存储
    //     key: 'my_shop_key'        // 自定义 localStorage 键名
    // }

    //指定key、存储方式、筛选数据
    persist: {
        storage: localStorage,
        getItemKey: (id) => `shopStore_${id}`, // 自定义 key 名，此api已经失效，推荐使用  key: 'my_shop_key'
        filter: ({ store }) => {
            return {
                shop: store.shop
            }
        }
    }

})
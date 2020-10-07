import Vue from 'vue'
import VueRouter from 'vue-router'
import MainPage from "pages/MainPage.vue"

import ProductList from "components/products/ProductList.vue";


Vue.use(VueRouter)

const routes = [
    { path: '/', component: MainPage },
    { path: '/setting', component: ProductList },
    { path: '*', component: MainPage },
]


export default new VueRouter({
    mode: 'history',
    routes
})
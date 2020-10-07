import Vue from 'vue'
import VueRouter from 'vue-router'
import CatalogCardComponent from "pages/CatalogCardComponent.vue"
import ProductList from "components/products/ProductList.vue"

Vue.use(VueRouter)

const routes = [
    { path: '/', component: CatalogCardComponent },
    { path: '/setting', component: ProductList },
    { path: '*', component: CatalogCardComponent },
]

export default new VueRouter({
    mode: 'history',
    routes
})
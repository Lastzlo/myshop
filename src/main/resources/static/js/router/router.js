import Vue from 'vue'
import VueRouter from 'vue-router'
import CatalogCardComponent from "pages/CatalogCardComponent.vue"
import ProductList from "components/products/ProductList.vue"
import FilterComponent from "pages/FilterComponent.vue";

Vue.use(VueRouter)

//отвечает за то какие компоненты и когда подргружать
const routes = [
    { path: '/', component: CatalogCardComponent },
    { path: '/setting', component: ProductList },
    { path: '/filter', component: FilterComponent },
    { path: '*', component: CatalogCardComponent },
]

//мод history убирает * строке запроса
export default new VueRouter({
    mode: 'history',
    routes
})
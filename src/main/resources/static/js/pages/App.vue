<template>
    <v-app>
        <v-app-bar>
            <v-toolbar-title>My Shop</v-toolbar-title>
        </v-app-bar>
        <v-main>
            <v-container style="height: 1500px;">
                <div>
                    <h4>Categories</h4>
                    <div>
                        <categories-list :categories="categories"/>
                    </div>
                    <h4>Brands</h4>
                    <div>
                        <brands-list :brands="brands"/>
                    </div>
                    <h4>Рroducts</h4>
                    <div>
                        <products-list :products="products"/>
                    </div>
                </div>
            </v-container>
        </v-main>
    </v-app>

</template>

<script>
    import CategoriesList from "components/categories/CategoriesList.vue"
    import BrandsList from "components/brands/BrandsList.vue";
    import ProductsList from "components/products/ProductsList.vue"
    export default {
        name: "App.vue",
        components: {
            CategoriesList,
            BrandsList,
            ProductsList,
        },
        data() {
            return{
                categories: [],
                brands: [],
                products: [],

            }
        },
        created: function () {
            //запрос на сервер
            this.$resource('/category{/id}').get().then(result =>
                result.json().then(data =>
                    //записать данные в products
                    data.forEach(category => this.categories.push(category))
                )
            )

            this.$resource('/brand{/id}').get().then(result =>
                result.json().then(data =>
                    //записать данные в products
                    data.forEach(brand => this.brands.push(brand))
                )
            )

            this.$resource('/product{/id}').get().then(result =>
                result.json().then(data =>
                    //записать данные в products
                    data.forEach(product => this.products.push(product))
                )
            )

        },
    }
</script>

<style scoped>

</style>
<template>
    <v-app>
        <v-app-bar app>
            <v-toolbar-title>My Shop</v-toolbar-title>
        </v-app-bar>
        <v-main>
            <!--<v-container>
                <v-tabs v-model="tabs">
                    <v-tab>Product</v-tab>
                    <v-tab>Brand</v-tab>
                    <v-tab>Category</v-tab>
                </v-tabs>

                <v-tabs-items v-model="tabs">
                    <v-tab-item>
                        <product-list :categories="categories"/>
                    </v-tab-item>
                    <v-tab-item>
                        <brands-list :categories="categories"/>
                    </v-tab-item>
                    <v-tab-item>
                        <categories-list :categories="categories"/>
                    </v-tab-item>


                </v-tabs-items>
            </v-container>-->
            <!--<directory-list/>-->
            <product-list/>
        </v-main>
    </v-app>

</template>

<script>
    import ProductList from "components/products/ProductList.vue";
    //import DirectoryList from "components/other/DirectoryList.vue";
    //import BrandsList from "components/brands/BrandsList.vue";
    //import CategoriesList from "components/categories/CategoriesList.vue";

    export default {
        name: "App.vue",
        components: {
            //DirectoryList,
            ProductList,
            //CategoriesList,
            //BrandsList
        },
        // data() {
        //     return{
        //         tabs: null,
        //         categories: [],
        //     }
        // },
        created: function () {
            //запрос на сервер
            // this.$resource('/category{/id}').get().then(result =>
            //     result.json().then(data =>
            //         //записать данные в products
            //         data.forEach(category => this.categories.push(category))
            //     )
            // )

            /*this.$resource('/brand{/id}').get().then(result =>
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
            )*/

        },
    }
</script>

<style scoped>

</style>
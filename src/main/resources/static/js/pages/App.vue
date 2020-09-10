<template>
    <v-app>
        <v-app-bar app>
            <v-toolbar-title>My Shop</v-toolbar-title>
        </v-app-bar>
        <v-main>
            <v-container>
                <v-tabs v-model="tabs">
                    <v-tab>Category</v-tab>
                    <v-tab>Brand</v-tab>
                    <v-tab>Product</v-tab>
                </v-tabs>

                <v-tabs-items v-model="tabs">
                    <v-tab-item>

                    </v-tab-item>
                    <v-tab-item>

                    </v-tab-item>
                    <v-tab-item>

                    </v-tab-item>

                </v-tabs-items>


            </v-container>

        </v-main>
    </v-app>

</template>

<script>

    export default {
        name: "App.vue",
        components: {

        },
        data() {
            return{
                tabs: null,
                categories: [],
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
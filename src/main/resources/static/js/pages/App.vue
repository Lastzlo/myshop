<template>
<!--    <v-app>-->
<!--        <v-app-bar app>-->
<!--            <v-toolbar-title>My Shop</v-toolbar-title>-->
<!--        </v-app-bar>-->
<!--        <v-main>-->
<!--            -->
<!--            <product-list/>-->
<!--        </v-main>-->
<!--        -->
<!--        -->
<!--        -->
<!--        -->
<!--    </v-app>-->

        <router-view></router-view>

</template>

<script>
    //import ProductList from "components/products/ProductList.vue";


    export default {
        name: "App.vue",
        components: {
            //ProductList,


        },
        // data() {
        //     return{
        //         tabs: null,
        //         categories: [],
        //     }
        // },
        created: function () {
        },
    }
</script>

<style scoped>

</style>
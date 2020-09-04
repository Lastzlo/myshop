<template>
    <div>
        <products-list :products="products"/>
    </div>
</template>

<script>
    import ProductsList from "components/products/ProductsList.vue";
    export default {
        name: "App.vue",
        components: {ProductsList},
        data() {
            return{
                products: []
            }
        },
        created: function () {
            //запрос на сервер
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
<template>
    <div>
        <h4>Categories</h4>
        <div>
            <categories-list :categories="categories"/>
        </div>
        <h4>Рroducts</h4>
        <div>
            <products-list :products="products"/>
        </div>
    </div>

</template>

<script>
    import CategoriesList from "components/categories/CategoriesList.vue"
    import ProductsList from "components/products/ProductsList.vue"
    export default {
        name: "App.vue",
        components: {
            CategoriesList,
            ProductsList,
        },
        data() {
            return{
                products: [],
                categories: []
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

            this.$resource('/category{/id}').get().then(result =>
                result.json().then(data =>
                    //записать данные в products
                    data.forEach(category => this.categories.push(category))
                )
            )
        },
    }
</script>

<style scoped>

</style>
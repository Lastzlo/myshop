<template>
    <div style="position: relative; width: 300px">
        <product-form :products="products" :productAttr="product" />
        <!--в message-row отправляем :product="product" :key="product.id" :editMethod="editMethod"-->
        <product-row v-for="product in products"
                     :key="product.id"
                     :product="product"
                     :editMethod="editMethod"
                     :products="products" />
    </div>
</template>

<script>
    import ProductForm from "components/products/ProductForm.vue";
    import ProductRow from "components/products/ProductRow.vue";

    export default {
        name: "ProductsList",
        props: ['products'],
        components: {
            ProductRow,
            ProductForm
        },
        data: function () {
            return{
                product: null
            }
        },
        methods: {
            editMethod: function (product) {
                this.product = product;
            }
        }
    }
</script>

<style scoped>

</style>
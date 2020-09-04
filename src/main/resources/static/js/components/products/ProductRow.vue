<template>
    <div>
        <i>({{ product.id }})</i> {{ product.productName }}  {{ product.productFile }}
        <span style="position: absolute; right: 0">
        <!--обработчик нажатия на кнопку @click="edit" работает как v-on:click="edit"-->
        <input type="button" value="Edit" @click="edit"/>
        <input type="button" value="X" @click="del"/>
        </span>
    </div>
</template>

<script>
    export default {
        name: "ProductRow",
        props: [
            'product',
            'editProduct',
            'deleteProduct'
        ],
        methods: {
            edit() {
                this.editProduct(this.product)
            },
            del() {
                this.deleteProduct(this.product)
            }
        }
    }
</script>

<style scoped>

</style>
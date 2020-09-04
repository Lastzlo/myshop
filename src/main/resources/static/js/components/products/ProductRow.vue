<template>
    <div>
        <i>({{ product.id }})</i> {{ product.productName }}  {{ product.file }}
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
        props: ['product', 'editMethod', 'products'],
        methods: {
            edit: function () {
                this.editMethod(this.product);
            },
            del: function () {
                this.$resource('/product{/id}').remove({id: this.product.id}).then(result => {
                    if (result.ok) {
                        this.products.splice(this.products.indexOf(this.product), 1)
                    }
                })
            }
        }
    }
</script>

<style scoped>

</style>
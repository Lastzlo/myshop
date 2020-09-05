<template>
    <div style="position: relative; width: 300px">
        <brand-form :brands="brands" :brandAttr="brand" />
        <!--в message-row отправляем :brand="brand" :key="brand.id" :editMethod="editMethod"-->
        <brand-row v-for="brand in brands"
                      :key="brand.id"
                      :brand="brand"
                      :editBrand="editBrand"
                      :deleteBrand="deleteBrand" />
    </div>
</template>

<script>
    import BrandForm from "components/brands/BrandForm.vue";
    import BrandRow from "components/brands/BrandRow.vue";

    export default {
        name: "BrandsList",
        props: ['brands'],
        components: {
            BrandRow,
            BrandForm
        },
        data: function () {
            return{
                brand: null
            }
        },
        methods: {
            editBrand(brand) {
                this.brand = brand
            },
            deleteBrand(brand) {
                this.$resource('/brand{/id}').remove({id: brand.id}).then(result => {
                    if (result.ok) {
                        this.brands.splice(this.brands.indexOf(brand), 1)
                    }
                })
            }
        }
    }
</script>

<style scoped>

</style>
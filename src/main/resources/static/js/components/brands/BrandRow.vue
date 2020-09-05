<template>
    <div>
        <i>({{ brand.id }})</i> {{ brand.brandName }}
        <span style="position: absolute; right: 0">
        <!--обработчик нажатия на кнопку @click="edit" работает как v-on:click="edit"-->
        <input type="button" value="Edit" @click="edit"/>
        <input type="button" value="X" @click="del"/>
        </span>
    </div>
</template>

<script>
    export default {
        name: "BrandRow",
        props: [
            'brand',
            'editBrand',
            'deleteBrand'
        ],
        methods: {
            edit() {
                this.editBrand(this.brand)
            },
            del() {
                this.deleteBrand(this.brand)
            }
        }
    }
</script>

<style scoped>

</style>
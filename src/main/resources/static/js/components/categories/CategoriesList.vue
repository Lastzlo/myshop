<template>
    <div style="position: relative; width: 300px">
        <category-form :categories="categories" :categoryAttr="category" />
        <!--в message-row отправляем :category="category" :key="category.id" :editMethod="editMethod"-->
        <category-row v-for="category in categories"
                      :key="category.id"
                      :category="category"
                      :editCategory="editCategory"
                      :deleteCategory="deleteCategory" />
    </div>
</template>

<script>
    import CategoryForm from "components/categories/CategoryForm.vue";
    import CategoryRow from "components/categories/CategoryRow.vue";

    export default {
        name: "CategoriesList",
        props: ['categories'],
        components: {
            CategoryRow,
            CategoryForm
        },
        data: function () {
            return{
                category: null
            }
        },
        methods: {
            editCategory(category) {
                this.category = category
            },
            deleteCategory(category) {
                this.$resource('/category{/id}').remove({id: category.id}).then(result => {
                    if (result.ok) {
                        this.categories.splice(this.categories.indexOf(category), 1)

                    }
                })
            }
        }
    }
</script>

<style scoped>

</style>
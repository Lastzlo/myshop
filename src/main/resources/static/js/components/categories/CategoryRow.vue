<template>
    <div>
        <i>({{ category.id }})</i> {{ category.categoryName }}
        <span style="position: absolute; right: 0">
        <!--обработчик нажатия на кнопку @click="edit" работает как v-on:click="edit"-->
        <input type="button" value="Edit" @click="edit"/>
        <input type="button" value="X" @click="del"/>
        </span>
    </div>
</template>

<script>
    export default {
        name: "CategoryRow",
        props: [
            'category',
            'editCategory',
            'deleteCategory'
        ],
        methods: {
            edit() {
                this.editCategory(this.category)
            },
            del() {
                this.deleteCategory(this.category)
            }
        }
    }
</script>

<style scoped>

</style>
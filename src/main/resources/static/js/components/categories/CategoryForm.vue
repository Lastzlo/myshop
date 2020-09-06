<template>
    <!--v-model="categoryName" записывает весь пользовательский ввод в переменную categoryName
    v-on:click="save" запускает анонимную функцию записаную в save-->
    <div>
        <input type="text" placeholder="Write something" v-model="categoryName"/>
        <!--обработчик нажатия на кнопку v-on:click="save"-->
        <input type="button" value="Save" v-on:click="save"/>
    </div>
</template>

<script>
    function getIndex(list, id) {
        for (var i=0; i<list.length; i++){
            if(list[i].id === id){
                return i;
            }
        }
        return -1;
    }

    export default {
        name: "CategoryForm",
        props: ['categories', 'categoryAttr'],
        data: function() {
            return {
                id: '',
                categoryName: '',   //переменная для текста
            }
        },
        watch: {
            categoryAttr: function (newVal, oldVal) {
                this.categoryName = newVal.categoryName
                this.id = newVal.id
            }
        },
        methods: {
            save: function () {

                let category = {categoryName: this.categoryName};

                if(this.id){
                    this.$resource('/category{/id}').update({id: this.id}, category).then(result =>
                        result.json().then(data => {
                            var index = getIndex(this.categories, data.id)
                            this.categories.splice(index, 1, data)
                            this.categoryName = ''
                            this.id = ''
                        })
                    )
                }
                else {
                    //saveProduction имеет кастомную ссылку 'category/create'
                    this.$resource('/category{/id}').save({}, category).then(
                        result =>
                            result.json().then(data => {
                                this.categories.push(data)
                                this.categoryName = ''
                            })
                    )
                }
            }
        }
    }
</script>

<style scoped>

</style>
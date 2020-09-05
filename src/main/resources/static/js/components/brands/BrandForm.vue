<template>
    <!--v-model="brandName" записывает весь пользовательский ввод в переменную brandName
    v-on:click="save" запускает анонимную функцию записаную в save-->
    <div>
        <input type="text" placeholder="Write something" v-model="brandName"/>
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
        name: "BrandForm",
        props: ['brands', 'brandAttr'],
        data: function() {
            return {
                id: '',
                brandName: '',   //переменная для текста
            }
        },
        watch: {
            brandAttr: function (newVal, oldVal) {
                this.brandName = newVal.brandName
                this.id = newVal.id
            }
        },
        methods: {
            save: function () {

                let brand = {brandName: this.brandName};

                if(this.id){
                    this.$resource('/brand{/id}').update({id: this.id}, brand).then(result =>
                        result.json().then(data => {
                            var index = getIndex(this.brands, data.id)
                            this.brands.splice(index, 1, data)
                            this.brandName = ''
                            this.id = ''
                        })
                    )
                }
                else {
                    //saveProduction имеет кастомную ссылку 'brand/create'
                    this.$resource('/brand{/id}').save({}, brand).then(
                        result =>
                            result.json().then(data => {
                                this.brands.push(data)
                                this.brandName = ''
                            })
                    )
                }
            }
        }
    }
</script>

<style scoped>

</style>
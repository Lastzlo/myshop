<template>
    <!--v-model="productName" записывает весь пользовательский ввод в переменную productName
    v-on:click="save" запускает анонимную функцию записаную в save-->
    <div>
        <input type="text" placeholder="Write something" v-model="productName"/>
        <input type="file" ref="fileupload" @change="fileSelected" />
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
        name: "ProductForm",
        props: ['products', 'productAttr'],
        data: function() {
            return {
                id: '',
                productName: '',   //переменная для текста
                file:'' //переменная для файла
            }
        },
        watch: {
            productAttr: function (newVal, oldVal) {
                this.productName = newVal.productName;
                this.id = newVal.id;

            }
        },
        methods: {
            save: function () {

                let product = {productName: this.productName};
                let formData = new FormData();

                formData.append('file',this.file)
                formData.append(
                    'product',
                    new Blob(
                        [JSON.stringify(product)
                        ],
                        {
                            type: "application/json"
                        }
                    )
                )

                if(this.id){
                    this.$resource('/product{/id}').update({id: this.id}, formData).then(result =>
                        result.json().then(data => {
                            var index = getIndex(this.products, data.id)
                            this.products.splice(index, 1, data)
                            this.productName = ''
                            this.id = ''

                            this.file = ''
                            this.$refs.fileupload.value=null;

                        })
                    )
                }
                else {
                    //saveProduction имеет кастомную ссылку 'product/create'
                    this.$resource('/product{/id}').save({}, formData).then(
                        result =>
                            result.json().then(data => {
                                this.products.push(data);
                                this.productName = '';

                                this.file = ''
                                this.$refs.fileupload.value=null;
                            })
                    )
                }
            },
            fileSelected:function($event){
                this.file =$event.target.files[0]
            }
        }
    }
</script>

<style scoped>

</style>
function getIndex(list, id) {
    for (var i=0; i<list.length; i++){
        if(list[i].id === id){
            return i;
        }
    }

    return -1;
}

var productApi = Vue.resource('/product{/id}')

//компонент в который буду записиваться данные
Vue.component('message-form', {
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
    template:
    // v-model="productName" записывает весь пользовательский ввод в переменную productName
    // v-on:click="save" запускает анонимную функцию записаную в save
        '<div>\n' +
        '           <input type="text" placeholder="Write something" v-model="productName"/>\n' +
        '           <input type="file" ref="fileupload" @change="fileSelected" />\n' +
        '           <input type="button" value="Save" v-on:click="save"/>\n' +     // обработчик нажатия на кнопку v-on:click="save"
        '</div>',
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
                productApi.update({id: this.id}, formData).then(result =>
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
                productApi.save({}, formData).then(
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
});


//выносим в отдельный компонент message-row
Vue.component('message-row', {
    props: ['product', 'editMethod', 'products'],
    template: '<div>'+
        '<i>({{ product.id }})</i> {{ product.productName }}  {{ product.file }}'+
        '<span style="position: absolute; right: 0">'+
        // обработчик нажатия на кнопку @click="edit" работает как v-on:click="edit"
        '<input type="button" value="Edit" @click="edit"/>'+
        '<input type="button" value="X" @click="del"/>'+
        '</span>'+
        '</div>',
    methods: {
        edit: function () {
            this.editMethod(this.product);
        },
        del: function () {
            productApi.remove({id: this.product.id}).then(result => {
                if (result.ok) {
                    this.products.splice(this.products.indexOf(this.product), 1)
                }
            })
        }
    }
});


//компонент который выводит все что связано с сообщениями
// Определяем новый компонент под именем products-list
Vue.component('products-list', {    //название компонента
    //список значений котоорые ожидает компонент
    props: ['products'],
    //:product="product" предает параметр product в message-row
    data: function () {
        return{
            product: null
        }
    },
    template:
        '<div style="position: relative; width: 300px">'+
        '<message-form :products="products" :productAttr="product" />'+
        //в message-row отправляем :product="product" :key="product.id" :editMethod="editMethod"
        '<message-row v-for="product in products" :key="product.id" :product="product" '+
        ':editMethod="editMethod" :products="products" />'+
        '</div>',
    //ХУКИ жизненого цикла //обычная анонимная функция, Например, хук created можно использовать для выполнения кода после создания экземпляра:
    created: function () {
        //запрос на сервер
        productApi.get().then(result =>
            /*console.log(result)*/
            result.json().then(data =>
                //вывести данные в консоль
                /*console.log(data),*/
                //записать данные в products
                data.forEach(product => this.products.push(product))
            )
        )
    },
    methods: {
        editMethod: function (product) {
            this.product = product;
        }
    }
});

var app = new Vue({
    el: '#app',
    //чтобы использовать компонент добавлем его в template
    // :products="products" параметр который мы передаем в message-list
    template: '<products-list :products="products"/>',
    data: {
        products: [
            /*{id: '123', productName:'Wow'},
            {id: '124', productName:'hey'},*/
        ]
    }
})



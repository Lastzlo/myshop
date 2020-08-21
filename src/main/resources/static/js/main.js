//добавляем кастомную ссылку для сохранения сообщения
/*https://github.com/pagekit/vue-resource/blob/develop/docs/resource.md*/
var messageApi = Vue.resource(
    'product{/id}',
    {},
    {
        saveProduct: {method: 'POST', url: 'product/create'}
    })

/*var messageApi = Vue.resource('product{/id}')*/

//компонент в который буду записиваться данные
Vue.component('message-form', {
    props: ['messages'],
    data: function() {
        return {
            text: '',   //переменная для текста
            file :'' //переменная для файла
        }
    },
    template:
    // v-model="text" записывает весь пользовательский ввод в переменную text
    // v-on:click="save" запускает анонимную функцию записаную в save
        '<div>\n' +
        '           <input type="text" placeholder="Write something" v-model="text"/>\n' +
        '           <input type="file" @change="fileSelected" />\n' +
        '           <input type="button" value="Save" v-on:click="save"/>\n' +
        '</div>',
    methods: {
        save: function () {

            let message = {name: this.text};
            let formData = new FormData();

            formData.append('file',this.file)
            formData.append(
                'product',
                new Blob(
                    [JSON.stringify(message)
                    ],
                    {
                        type: "application/json"
                    }
                )
            )

            //saveProduction имеет кастомную ссылку 'product/create'
            messageApi.saveProduct({}, formData).then(
                result =>
                    result.json().then(data => {
                        this.messages.push(data);
                        this.text = '';
                    })
            )
        },

        fileSelected:function($event){
            this.file =$event.target.files[0]
        }

    }
});

//комопонент отвечат за вывод одного сообщения
Vue.component(
    'message-row',
    {
        props: ['message'],
        template: '<div><i>({{ message.id }})</i> Name: {{ message.name }} picture: {{ message.productPicture }}</div>',
    }
)

//компонент который выводит все что связано с сообщениями
// Определяем новый компонент под именем messages-list
Vue.component('messages-list', {    //название компонента
    props: ['messages'],    //список значений котоорые ожидает компонент
    //:message="message" предает параметр message в message-row
    template:
        '<div>' +
        '<message-form :messages="messages" />'+
        '<message-row v-for="message in messages" :message="message" :key="message.id" ></message-row>' +
        '</div>',
    //created - это хук который срабатывает перед рендером
    created: function () {
        //запрос на сервер
        messageApi.get().then(result =>
            result.json().then(data =>
                //вывести данные в консоль
                /*console.log(data),*/
                //записать данные в messages
                data.forEach(message => this.messages.push(message))
            )
        )
    }
})

var app = new Vue({
    el: '#app',
    template: '<messages-list :messages="messages"/>',   //чтобы использовать компонент добавлем его в template: , :messages="messages" параметр который мы передаем в message-list
    data: {
        messages: [
            {id: '123', name:'Wow'},
            {id: '124', name:'hey'},
        ]
    }
})
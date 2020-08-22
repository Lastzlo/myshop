function getIndex(list, id) {
    for (var i=0; i<list.length; i++){
        if(list[i].id === id){
            return i;
        }
    }

    return -1;
}

//добавляем кастомную ссылку для сохранения сообщения
/*https://github.com/pagekit/vue-resource/blob/develop/docs/resource.md*/
var messageApi = Vue.resource(
    'product{/id}',
    {},
    {
        saveProduct: {method: 'POST', url: 'product/create'}
    })

//компонент в который буду записиваться данные
Vue.component('message-form', {
    props: ['messages', 'messageAttr'],
    data: function() {
        return {
            id: '',
            text: '',   //переменная для текста
            file:'' //переменная для файла
        }
    },
    watch: {
        messageAttr: function (newVal, oldVal) {
            this.text = newVal.text;
            this.id = newVal.id;

        }
    },
    template:
    // v-model="text" записывает весь пользовательский ввод в переменную text
    // v-on:click="save" запускает анонимную функцию записаную в save
        '<div>\n' +
        '           <input type="text" placeholder="Write something" v-model="text"/>\n' +
        '           <input type="file" ref="fileupload" @change="fileSelected" />\n' +
        '           <input type="button" value="Save" v-on:click="save"/>\n' +     // обработчик нажатия на кнопку v-on:click="save"
        '</div>',
    methods: {
        save: function () {

            let message = {text: this.text};
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

            if(this.id){
                messageApi.update({id: this.id}, formData).then(result =>
                    result.json().then(data => {
                        var index = getIndex(this.messages, data.id)
                        this.messages.splice(index, 1, data)
                        this.text = ''
                        this.id = ''

                        this.file = ''
                        this.$refs.fileupload.value=null;

                    })
                )
            }
            else {
                //saveProduction имеет кастомную ссылку 'product/create'
                messageApi.saveProduct({}, formData).then(
                    result =>
                        result.json().then(data => {
                            this.messages.push(data);
                            this.text = '';


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
    props: ['message', 'editMethod', 'messages'],
    template: '<div>'+
        '<i>({{ message.id }})</i> {{ message.text }}  {{ message.file }}'+
        '<span style="position: absolute; right: 0">'+
        // обработчик нажатия на кнопку @click="edit" работает как v-on:click="edit"
        '<input type="button" value="Edit" @click="edit"/>'+
        '<input type="button" value="X" @click="del"/>'+
        '</span>'+
        '</div>',
    methods: {
        edit: function () {
            this.editMethod(this.message);
        },
        del: function () {
            messageApi.remove({id: this.message.id}).then(result => {
                if (result.ok) {
                    this.messages.slice(this.messages.indexOf(this.message), 1)
                }
            })
        }
    }
});


//компонент который выводит все что связано с сообщениями
// Определяем новый компонент под именем messages-list
Vue.component('messages-list', {    //название компонента
    //список значений котоорые ожидает компонент
    props: ['messages'],
    //:message="message" предает параметр message в message-row
    data: function () {
        return{
            message: null
        }
    },
    template:
        '<div style="position: relative; width: 300px">'+
        '<message-form :messages="messages" :messageAttr="message" />'+
        //в message-row отправляем :message="message" :key="message.id" :editMethod="editMethod"
        '<message-row v-for="message in messages" :key="message.id" :message="message" '+
        ':editMethod="editMethod" :messages="messages" />'+
        '</div>',
    //ХУКИ жизненого цикла //обычная анонимная функция, Например, хук created можно использовать для выполнения кода после создания экземпляра:
    created: function () {
        //запрос на сервер
        messageApi.get().then(result =>
            /*console.log(result)*/
            result.json().then(data =>
                //вывести данные в консоль
                /*console.log(data),*/
                //записать данные в messages
                data.forEach(message => this.messages.push(message))
            )
        )
    },
    methods: {
        editMethod: function (message) {
            this.message = message;
        }
    }
});

var app = new Vue({
    el: '#app',
    //чтобы использовать компонент добавлем его в template
    // :messages="messages" параметр который мы передаем в message-list
    template: '<messages-list :messages="messages"/>',
    data: {
        messages: [
            /*{id: '123', text:'Wow'},
            {id: '124', text:'hey'},*/
        ]
    }
})



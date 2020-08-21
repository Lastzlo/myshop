var messageApi = Vue.resource('product{/id}')

//комопонент отвечат за вивод одного сообщения
Vue.component(
    'message-row',
    {
        props: ['message'],
        template: '<div><i>({{ message.id }})</i> {{ message.name }}</div>',
    }
)


// Определяем новый компонент под именем messages-list
Vue.component('messages-list', {    //название компонента
    props: ['messages'],    //список значений которые мы ожидаем
    //:message="message" предает параметр message в message-row
    template:
        '<div>' +
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
    template: '<messages-list :messages="messages"/>',   //чтобы использовать компонент добавлем его в template: , :messages="messages" предает параметр message в message-list
    data: {
        messages: [
            {id: '123', name:'Wow'},
            {id: '124', name:'hey'},
        ]
    }
})
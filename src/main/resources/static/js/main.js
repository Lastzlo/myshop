var messageApi = Vue.resource('product{/id}')

//комопонент отвечат за вивод одного сообщения
Vue.component(
    'message-row',
    {
        props: ['message'],
        template: '<div><i>({{ message.id }})</i> {{ message.text }}</div>',
    }
)


// Определяем новый компонент под именем messages-list
Vue.component('messages-list', {    //название компонента
    props: ['messages'],    //список значений которые мы ожидаем
    template:
        '<div>' +
            '<message-row v-for="message in messages" :message="message" :key="message.id" ></message-row>' +
        '</div>'  //:message="message" предает параметр message в message-row
})


var app = new Vue({
    el: '#app',
    template: '<messages-list :messages="messages"/>',   //чтобы использовать компонент добавлем его в template: , :messages="messages" предает параметр message в message-list
    data: {
        messages: [
            {id: '123', text:'Wow'},
            {id: '124', text:'hey'},
        ]
    }
})
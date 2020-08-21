


// Определяем новый компонент под именем messages-list
Vue.component('messages-list', {    //название компонента
    props: ['messages'],    //список значений которые мы ожидаем
    template: '<div><div v-for="message in messages">{{ message.text }}</div></div>'
})


var app = new Vue({
    el: '#app',
    template: '<messages-list :messages="messages"/>',   //чтобы использовать компонент добавлем его в template: , чтобы передать значения пишем :messages="messages"
    data: {
        messages: [
            {id: '123', text:'Wow'},
            {id: '124', text:'hey'},
        ]
    }
})
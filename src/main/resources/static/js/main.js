import Vue from 'vue'
import Vuetify from 'vuetify'
import VueResource from 'vue-resource'
import App from 'pages/App.vue'
import MainPage from "pages/MainPage.vue"


Vue.use(Vuetify)
Vue.use(VueResource);

new Vue({
    el: '#app',
    vuetify: new Vuetify(),
    render: a => a(App)
})

new Vue({
    el: '#mainPage',
    vuetify: new Vuetify(),
    render: a => a(MainPage)
})


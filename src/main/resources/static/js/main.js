import Vue from 'vue'
import Vuetify from 'vuetify'
import VueResource from 'vue-resource'
import App from 'pages/App.vue'
//import MainPage from "pages/MainPage.vue";

import router from "router/router.js"

Vue.use(Vuetify)
Vue.use(VueResource);

new Vue({
    el: '#app',
    router,
    vuetify: new Vuetify(),
    render: a => a(App)
})

// new Vue({
//     el: '#mainPage',
//     vuetify: new Vuetify(),
//     render: a => a(MainPage)
// })

